import styled from "styled-components";
import Button from "../components/common/Button";
import { login } from "../api/login";
import { signup } from "../api/signup";

const AuthFormBlock = styled.div``;

const StyledInput = styled.input`
    font-size: 1rem;
    border: none;
    border-bottom: 1px solid gray;
    padding-bottom: 0.5rem;
    outline: none;
    width: 100%;
    &:focus {
        color: $oc-teal-7;
        border-bottom: 1px solid black;
    }
`;

const authFormSubmitHandler = async event => {
    event.preventDefault();
    const submitFormType = event.currentTarget.getAttribute("type");
    const formData = new FormData(event.currentTarget);
    if (submitFormType === "login") {
        // 로그인
        const loginPayload = {
            email: formData.get("email"),
            password: formData.get("password"),
        };
        const loginRes = await login(loginPayload);
        if (loginRes === "fail") return;
        // const userInfo = await getCurrentUserInfo();
        // if (userInfo === null) return;
        alert("success");
    } else {
        // 회원가입
        const signupPayload = {
            email: formData.get("email"),
            password: formData.get("password"),
            username: formData.get("username"),
        };
        console.log(signupPayload);
        const signupRes = await signup(signupPayload);
        if (signupRes === "fail") return;
        alert("success");
    }
};

const textMap = {
    login: "로그인",
    signup: "회원가입",
};

const AuthForm = ({ type }) => {
    const text = textMap[type];
    return (
        <AuthFormBlock>
            <form onSubmit={authFormSubmitHandler} type={type}>
                <label>
                    <StyledInput autoComplete="email" type="email" name="email" placeholder="이메일" />
                </label>
                <label>
                    <StyledInput autoComplete="password" type="password" name="password" placeholder="비밀번호" />
                </label>
                {type === "signup" && (
                    <>
                        <label>
                            <StyledInput autoComplete="passwordConfirm" type="password" name="passwordConfirm" placeholder="비밀번호 확인" />
                        </label>
                        <label>
                            <StyledInput autoComplete="username" type="text" name="username" placeholder="이름" />
                        </label>
                    </>
                )}
                <Button pink fullWidth>
                    {text}
                </Button>
            </form>
        </AuthFormBlock>
    );
};

export default AuthForm;
