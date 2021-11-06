import logo from './logo.svg';
import './App.css';

import React from 'react';
import Tasks from "./components/Tasks";
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

    handleAuthorization(auth) {
        console.log("handle authorization");
        console.log("authEvent", auth);
        this.setState({authorization: auth});
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
                    <li><button onClick={() => this.updateDashboard(<Tasks/>)}>Задачи</button></li>
                    <li><button onClick={() => this.updateDashboard(<Account/>)}>Счет</button></li>
                </ul>
                {this.state.dashboard}
            </div>
        );
    }
}

export default App;
