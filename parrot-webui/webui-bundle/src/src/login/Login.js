import React from 'react';
import env from "react-dotenv";

function Login(props) {
    let login;

    function handleSubmit() {
        fetch(env.API_URL + "/auth/login?login=" + login, {
            method: 'POST'
        }).catch(r => console.log(r))
            .then(r => r.headers.get("Authorization"))
            .then(auth => console.log("Authorization = " + auth));

    }

    function handleChange(event) {
        login = event.target.value;
    }

    return (
        <form action={env.API_URL + "/auth/login"} method="POST">
            <label>
                Имя: <input type="text" name="login" value={login} onChange={handleChange}/>
            </label>
            <input type="submit" value="Отправить"/>
        </form>
    );
}

export default Login;
