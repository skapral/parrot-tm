import jwtDecode from 'jwt-decode';

const decoder = new TextDecoder();

function Login(props) {
    let auth = props.authorization;
    if(!!auth) {
        return <p>Залогинен {auth}</p>
    } else {
        return <p>Не залогинен</p>
    }
}

export default Login;
