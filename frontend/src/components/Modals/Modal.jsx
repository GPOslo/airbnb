import React from "react";
import "../../assets/css/modal.css";

const Modal = props => {
    const { open, close, header, children } = props;

    return (
        <div className={open ? "openModal modal" : "modal"}>
            {open ? (
                <section>
                    <header>
                        {header}
                        <button className="close" onClick={close}>
                            x
                        </button>
                    </header>
                    <main>{children}</main>
                    <footer>
                        <button className="close" onClick={close}>
                            close
                        </button>
                    </footer>
                </section>
            ) : null}
        </div>
    );
};

export default Modal;
