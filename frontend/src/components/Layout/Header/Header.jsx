import { useState } from "react";
import styled from "styled-components";
import AuthForm from "../../../auth/AuthForm";
import AuthTemplate from "../../../auth/AuthTemplate";
import { BASE_URL } from "../../../config";
import { useRouter } from "../../../hooks/useRouter";
import { getAccessTokenFromLocalStorage, saveAccessTokenToLocalStorage } from "../../../utils/accessTokenHandler";
import { getRefreshTokenFromMemory, saveRefreshTokenToMemory } from "../../../utils/refreshTokenHandler";
import Modal from "../../Modals/Modal";

const HeaderWrapper = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
`;

const Header = () => {
    const [modalOpen, setModalOpen] = useState(false);
    const [signupForm, setSignupForm] = useState(false);

    const { routeTo } = useRouter();

    const changeFormType = () => {
        setSignupForm(!signupForm);
    };
    const openModal = () => {
        setModalOpen(true);
    };
    const closeModal = () => {
        setModalOpen(false);
    };

    const onLogoClick = async e => {
        const accessToken = getAccessTokenFromLocalStorage();
        if (!accessToken) {
            console.log("로그인 전입니다.");
            return;
        }
        const reloadRes = await fetch(BASE_URL + "/", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${accessToken}`,
            },
        });
        if (reloadRes.ok) {
            console.log("access token 만료 전");
        } else {
            const { message, object } = await reloadRes.json();
            if (object === "EXPIRED_TOKEN") {
                const reissueRes = await fetch(BASE_URL + "/auth/reissue", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${accessToken}`,
                    },
                    body: JSON.stringify({
                        accessToken: accessToken,
                        refreshToken: getRefreshTokenFromMemory(),
                    }),
                });

                if (reissueRes.ok) {
                    const { accessToken, grantType, refreshToken } = await reissueRes.json();
                    saveAccessTokenToLocalStorage(accessToken);
                    saveRefreshTokenToMemory(refreshToken);
                } else {
                    alert("재발급 실패");
                    return;
                }
            }
        }
    };

    return (
        <HeaderWrapper>
            <button onClick={onLogoClick}> 로고자리 </button>
            <button
                onClick={() => {
                    routeTo("/host/homes");
                }}
            >
                당신의 공간의 에어비앤비하세요
            </button>

            <button onClick={openModal}>{getAccessTokenFromLocalStorage() ? "사용자" : "로그인 / 회원가입"}</button>
            <Modal open={modalOpen} close={closeModal} header="로그인 또는 회원가입하기">
                <AuthTemplate>
                    <AuthForm type={signupForm ? "signup" : "login"} />
                </AuthTemplate>
                <p onClick={changeFormType}> {signupForm ? "회원가입" : "로그인"}</p>
            </Modal>
        </HeaderWrapper>
    );
};
export default Header;
