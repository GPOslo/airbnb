import { BASE_URL, AUTH } from "../config";
import { saveAccessTokenToLocalStorage } from "../utils/accessTokenHandler";
import { saveRefreshTokenToMemory } from "../utils/refreshTokenHandler";

export const login = async args => {
    const loginRes = await fetch(BASE_URL + AUTH.LOGIN, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(args),
    });
    if (loginRes.ok) {
        const { accessToken, refreshToken, grantType } = await loginRes.json();
        saveAccessTokenToLocalStorage(accessToken);
        saveRefreshTokenToMemory(refreshToken);
        return "success";
    }
    return "fail";
};

export const getCurrentUserInfo = async () => {
    const userInfoRes = await fetch(AUTH.PROFILE, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
    });
    if (userInfoRes.ok) {
        return userInfoRes.json();
    }
    return null;
};
