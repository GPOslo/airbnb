import styled, { css } from "styled-components";

const StyledButton = styled.button`
    border: none;
    border-radius: 4px;
    font-size: 1rem;
    font-weight: bold;
    padding: 0.25rem 1rem;
    color: white;
    outline: none;
    cursor: pointer;

    ${props =>
        props.fullWidth &&
        css`
            padding-top: 0.75rem;
            paaind-bottom: 0.75rem;
            width: 100%;
            font-size: 1.125rem;
        `}
    ${props =>
        props.pink &&
        css`
            background: rgb(206, 52, 98);
        `}
`;

const Button = props => <StyledButton {...props} />;
export default Button;
