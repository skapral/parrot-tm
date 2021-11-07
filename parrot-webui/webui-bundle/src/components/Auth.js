import React from 'react';

function Auth(props) {
    console.log("props", props);

    let login;

    function handleSubmit(event) {
        event.preventDefault();
        fetch("/auth/login?login=" + login, {
            method: 'POST'
        })
            .then(response => {
                if (!response.ok) {
                    return Promise.reject(response);
                }
                return response;
            })
            .then(r => r.headers.get("Authorization"))
            .then(auth => { console.log("Authorization = " + auth); return auth; })
            .then(auth => { localStorage.setItem('jwt', auth); return auth; })
            .catch(r => { localStorage.removeItem('jwt'); })
            .finally(() => props.onAuthorized());
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
