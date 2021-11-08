import logo from './logo.svg';
import './App.css';

import React from 'react';
import Tasks from "./components/Tasks";
import Users from "./components/Users";
import Account from "./components/Account";
import Auth from "./components/Auth"
import Login from "./components/Login"

class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            authorization: "",
            dashboard: <div/>
        };
        this.handleAuthorization = this.handleAuthorization.bind(this);
    }

    handleAuthorization() {
        console.log("handle authorization");
        fetch("/auth/current", {
            headers: {
                Authorization: localStorage.getItem("jwt")
            }
        }).then(response => {
            if (!response.ok) {
                return Promise.reject(response);
            }
            return response;
        })
        .then(r => {
            let b = r.json().then(b => {
                console.log("b", b);
                this.setState({authorization: b.login + " " + b.role});
            });
        })
        .catch(r => {
            this.setState({authorization: null});
        });
    }

    updateDashboard(component) {
        console.log("dashboard", component);
        this.setState({dashboard: component});
    }

    render() {
        return (
            <div className="App">
                <div>
                    <Login authorization={this.state.authorization}/>
                </div>
                <div>Попка кросавчег :)</div>
                <ul>
                    <li><button onClick={() => this.updateDashboard(<Auth onAuthorized={this.handleAuthorization}/>)}>Авторизация</button></li>
                    <li><button onClick={() => this.updateDashboard(<Users/>)}>Пользователи</button></li>
                    <li><button onClick={() => this.updateDashboard(<Tasks/>)}>Задачи</button></li>
                    <li><button onClick={() => this.updateDashboard(<Account/>)}>Счет</button></li>
                </ul>
                {this.state.dashboard}
            </div>
        );
    }
}

export default App;
