import jwtDecode from 'jwt-decode';

const decoder = new TextDecoder();

function Login(props) {
    console.log("LOGIN", props);
    let auth = props.authorization;
    if(!!auth && auth.startsWith("Bearer ")) {
        auth = auth.replace("Bearer ", "");
        auth = jwtDecode(auth);
        console.log("auth", auth);
        return <p>Залогинен {auth.sub} {auth.role}</p>
    } else {
        return <p>Не залогинен</p>
    }
}

export default Login;
