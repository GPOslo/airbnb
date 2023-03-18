let refreshTokenInMemory = "";

export const saveRefreshTokenToMemory = refreshToken => {
    refreshTokenInMemory = refreshToken;
};
export const getRefreshTokenFromMemory = () => {
    return refreshTokenInMemory;
    // return localStorage.getItem("refreshToken") || "";
};
