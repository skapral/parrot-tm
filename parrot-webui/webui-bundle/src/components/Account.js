import React from "react";

class Account extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            description: "",
            money: undefined,
            log: []
        };
    }

    componentDidMount() {
        fetch("/accounting/transactions", {
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
                this.setState({log: b});
            });
        });
        fetch("/accounting/", {
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
                this.setState({money: b.money});
            });
        });
    }

    format_time(s) {
        const dtFormat = new Intl.DateTimeFormat('en-US');

        return new Date(s).toLocaleString();
    }

    render() {
        return (
            <div className="Account">
                <p>Ваш счет - {this.state.money}</p>

                <p>Список транзакций за прошедшие сутки</p>
                <table>
                    <thead>
                    <tr>
                        <td>Время</td>
                        <td>Описание</td>
                        <td>Дебет</td>
                        <td>Кредит</td>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        this.state.log.map(t => {
                            return <tr>
                                <td>{this.format_time(t.time)}</td>
                                <td>{t.description}</td>
                                <td>{t.debit}</td>
                                <td>{t.credit}</td>
                            </tr>
                        })
                    }
                    </tbody>
                </table>
            </div>
        );
    }
}

export default Account;
