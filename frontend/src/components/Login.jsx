import { useState } from 'react';
import { useSesion } from '../context/SesionContext.jsx';

export default function Login({ alRegistrarse }) {
    const { iniciarSesion } = useSesion();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [enviando, setEnviando] = useState(false);

    const enviar = async (e) => {
        e.preventDefault();
        setError('');
        setEnviando(true);
        try {
            await iniciarSesion(username, password);
        } catch (err) {
            setError(err.message);
        } finally {
            setEnviando(false);
        }
    };

    return (
        <main className="acceso">
            <form className="tarjeta-acceso" onSubmit={enviar}>
                <h1 className="marca">Te<span>Toca</span></h1>
                <p className="eslogan">Se hace lo que se puede.</p>
                <label>Usuario
                    <input value={username} onChange={(e) => setUsername(e.target.value)} required autoComplete="username" />
                </label>
                <label>Contraseña
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required autoComplete="current-password" />
                </label>
                {error && <p className="error" role="alert">{error}</p>}
                <button className="boton" disabled={enviando}>{enviando ? 'Entrando…' : 'Iniciar sesión'}</button>
                <button type="button" className="enlace" onClick={alRegistrarse}>
                    ¿No tienes cuenta? Regístrate con el código de tu piso
                </button>
            </form>
        </main>
    );
}