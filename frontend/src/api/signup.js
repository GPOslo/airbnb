import { BASE_URL, AUTH } from "../config";

export const signup = async args => {
    // if (password !== passwordConfirm) {
    //     alert("비밀번호가 다릅니다.");
    //     return false;
    // }

    const signupRes = await fetch(BASE_URL + AUTH.SIGNUP, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(args),
    });
    if (signupRes.ok) {
        const [email, username] = await signupRes.json();
        console.log(email, username);
        return "success";
    }
    return "fail";
};
