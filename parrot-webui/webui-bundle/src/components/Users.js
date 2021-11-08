import React from "react";

class Users extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            user: "",
            role: "PARROT",
            users: []
        };
        this.handleUserChange = this.handleUserChange.bind(this);
        this.handleRoleChange = this.handleRoleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        fetch("/auth/users/", {
            method: "GET",
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
            r.json().then(b => {
                this.setState({users: b});
            });
        });
    }


    handleUserChange(event) {
        this.setState({user: event.target.value});
    }

    handleRoleChange(event) {
        this.setState({role: event.target.value});
    }

    handleSubmit(event) {
        event.preventDefault();
        fetch("/auth/register", {
            body: JSON.stringify({login: this.state.user, role: this.state.role}),
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                Authorization: localStorage.getItem("jwt")
            }
        }).finally(() => {
            this.componentDidMount();
        });
    }

    render() {
        return <div className="Users">
            Пользователи

            <form onSubmit={e => this.handleSubmit(e)}>
                <label>
                    Имя: <input type="text" value={this.state.user} onChange={this.handleUserChange}/>
                    Роль: <select value={this.state.role} onChange={this.handleRoleChange}>
                        <option>PARROT</option>
                        <option>MANAGER</option>
                        <option>ADMIN</option>
                    </select>
                </label>
                <input type="submit" value="Создать"/>
            </form>

            <table>
                <thead>
                <tr>
                    <td>Id</td>
                    <td>Имя</td>
                    <td>Роль</td>
                </tr>
                </thead>
                <tbody>
                {
                    this.state.users.map(t => {
                        return <tr>
                            <td>{t.id}</td>
                            <td>{t.login}</td>
                            <td>{t.role}</td>
                        </tr>
                    })
                }
                </tbody>
            </table>
        </div>
    }
}

export default Users;