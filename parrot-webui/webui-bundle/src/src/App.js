import logo from './logo.svg';
import './App.css';

import React from 'react';
import Tasks from "./tasks/Tasks";
import Account from "./account/Account";
import Login from "./login/Login"


class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            authorization: "",
            dashboard: new Login()
        };
    }

    updateDashboard(component) {
        console.log(component);
        this.setState({dashboard: component});
    }

    render() {
        return (
            <div className="App">
                <div>Попка кросавчег :)</div>
                <ul>
                    <li><button onClick={() => this.updateDashboard(Login())}>Логин</button></li>
                    <li><button onClick={() => this.updateDashboard(Tasks())}>Задачи</button></li>
                    <li><button onClick={() => this.updateDashboard(Account())}>Счет</button></li>
                </ul>
                {this.state.dashboard}
            </div>
        );
    }
}

export default App;
