import React from 'react';

class Auth extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            login: "",
            users: []
        };

        this.onAuthorized = props.onAuthorized
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({login : event.target.value});
    }

    handleSubmit(event) {
        event.preventDefault();
        fetch("/auth/login?login=" + this.state.login, {
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
            .finally(() => this.onAuthorized());
    }


    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <label>
                    Имя: <input type="text" name="login" value={this.state.login} onChange={this.handleChange}/>
                </label>
                <input type="submit" value="Отправить"/>
            </form>
        );
    }
}

export default Auth;
