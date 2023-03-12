import { useState } from "react";
import styled from "styled-components";
import AuthForm from "../../../auth/AuthForm";
import AuthTemplate from "../../../auth/AuthTemplate";
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

    const changeFormType = () => {
        setSignupForm(!signupForm);
    };
    const openModal = () => {
        setModalOpen(true);
    };
    const closeModal = () => {
        setModalOpen(false);
    };

    return (
        <HeaderWrapper>
            <div> 로고자리 </div>
            <div>당신의 공간의 에어비앤비하세요</div>
            <button onClick={openModal}>로그인/회원가입</button>
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
