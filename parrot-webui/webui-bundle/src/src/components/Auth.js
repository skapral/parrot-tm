import React from 'react';
import env from "react-dotenv";

function Auth(props) {
    console.log("props", props);

    let login;

    function handleSubmit(event) {
        event.preventDefault();
        fetch("/auth/login?login=" + login, {
            method: 'POST'
        })
            .then(r => r.headers.get("Authorization"))
            .then(auth => { console.log("Authorization = " + auth); return auth; })
            .then(auth => props.onAuthorized(auth));
    }

    function handleChange(event) {
        console.log("handleChange", event.target.value)
        login = event.target.value;
    }

    return (
        <form onSubmit={handleSubmit}>
            <label>
                Имя: <input type="text" name="login" value={login} onChange={handleChange}/>
            </label>
            <input type="submit" value="Отправить"/>
        </form>
    );
}

export default Auth;
