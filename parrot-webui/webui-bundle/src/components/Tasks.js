import React from "react";

class Tasks extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            tasks: []
        };
    }

    componentDidMount() {
        fetch("/tasks/", {
            method: "GET",
            headers: {
                Authorization: localStorage.getItem("jwt")
            }
        })
            .then(response => {
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

    handleSubmit(event, description) {
        event.preventDefault();
        fetch("/tasks/?description=" + description, {
            method: "POST",
            headers: {
                Authorization: localStorage.getItem("jwt")
            }
        }).finally(() => {
            this.componentDidMount();
        });
    }

    render() {
        var description;

        var handleChange = event => description = event.target.value;

        console.log("this.state", this.state.tasks)
        return <div className="Tasks">
            Задачи

            <form onSubmit={e => this.handleSubmit(e, description)}>
                <label>
                    Имя: <input type="text" name="description" value={description} onChange={handleChange}/>
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
