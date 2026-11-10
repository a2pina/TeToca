import { useState } from 'react';
import { api } from '../api/cliente';
import { useSesion } from '../context/SesionContext';

const VACIO = { codigoPiso: '', nombrePiso: '', nombre: '', username: '', email: '', password: '' };

export default function Registro({ alVolver }) {
    const { iniciarSesion } = useSesion();
    const [datos, setDatos] = useState(VACIO);
    const [error, setError] = useState('');
    const [enviando, setEnviando] = useState(false);
    const [modo, setModo] = useState('unirse');

    const cambiar = (campo) => (e) => setDatos({ ...datos, [campo]: e.target.value });

    const enviar = async (e) => {
        e.preventDefault();
        setError('');
        setEnviando(true);
        try {
            const cuerpo = modo === 'crear'
                ? { ...datos, codigoPiso: null }
                : { ...datos, nombrePiso: null };
            await api('/auth/registro', { method: 'POST', body: cuerpo });
            await iniciarSesion(datos.username, datos.password);
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
                <p className="eslogan">Únete a tu piso o crea uno nuevo</p>

                <div className="selector-modo">
                    <button type="button" className={modo === 'unirse' ? 'boton' : 'boton boton-secundario'}
                            onClick={() => setModo('unirse')}>Unirme a un piso</button>
                    <button type="button" className={modo === 'crear' ? 'boton' : 'boton boton-secundario'}
                            onClick={() => setModo('crear')}>Crear un piso</button>
                </div>

                {modo === 'unirse'
                    ? <label>Código del piso<input value={datos.codigoPiso} onChange={cambiar('codigoPiso')} required /></label>
                    : <label>Nombre del piso<input value={datos.nombrePiso} onChange={cambiar('nombrePiso')} required /></label>}

                <label>Nombre<input value={datos.nombre} onChange={cambiar('nombre')} required /></label>
                <label>Usuario<input value={datos.username} onChange={cambiar('username')} required minLength={3} /></label>
                <label>Email<input type="email" value={datos.email} onChange={cambiar('email')} required /></label>
                <label>Contraseña<input type="password" value={datos.password} onChange={cambiar('password')} required minLength={6} /></label>
                {error && <p className="error" role="alert">{error}</p>}
                <button className="boton" disabled={enviando}>{enviando ? 'Creando cuenta…' : 'Crear cuenta'}</button>
                <button type="button" className="enlace" onClick={alVolver}>Ya tengo cuenta</button>
            </form>
        </main>
    );
}