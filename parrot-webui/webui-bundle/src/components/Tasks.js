import React from "react";

class Tasks extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            description: "",
            tasks: []
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        fetch("/tasks/", {
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
                this.setState({tasks: b});
            });
        });
    }

    handleChange(event) {
        this.setState({description: event.target.value});
    }

    handleSubmit(event) {
        event.preventDefault();
        fetch("/tasks/?description=" + this.state.description, {
            method: "POST",
            headers: {
                Authorization: localStorage.getItem("jwt")
            }
        }).finally(() => {
            this.componentDidMount();
        });
    }

    render() {
        return <div className="Tasks">
            Задачи

            <form onSubmit={this.handleSubmit}>
                <label>
                    Имя: <input type="text" name="description" value={this.state.description} onChange={this.handleChange}/>
                </label>
                <input type="submit" value="Создать"/>
            </form>

            <table>
                <thead>
                    <tr>
                        <td>Id</td>
                        <td>Описание</td>
                    </tr>
                </thead>
                <tbody>
                    {
                        this.state.tasks.map(t => {
                            return <tr>
                                <td>{t.id}</td>
                                <td>{t.description}</td>
                            </tr>
                        })
                    }
                </tbody>
            </table>
        </div>
    }
}

export default Tasks;
