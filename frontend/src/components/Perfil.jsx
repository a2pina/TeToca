import { useState } from 'react';
import { api } from '../api/cliente';
import { useSesion } from '../context/SesionContext';
import Avatar from './Avatar';

const OPCIONES = ['🦊', '🐼', '🐸', '🦉', '🐙', '🦄', '🐢', '🐝', '🌵', '🍕', '🍉', '🥑', '🚀', '🎸', '⚡', '🌙'];

export default function Perfil() {
    const { usuario, actualizarUsuario } = useSesion();
    const [nombre, setNombre] = useState(usuario.nombre);
    const [avatar, setAvatar] = useState(usuario.avatar ?? '');
    const [error, setError] = useState('');
    const [guardado, setGuardado] = useState(false);
    const [enviando, setEnviando] = useState(false);

    const guardar = async (e) => {
        e.preventDefault();
        setError('');
        setGuardado(false);
        setEnviando(true);
        try {
            // La API devuelve el usuario actualizado y lo guardamos en la sesión:
            // la cabecera y el resto de la app se repintan con los datos nuevos
            const actualizado = await api('/auth/yo', { method: 'PUT', body: { nombre, avatar: avatar || null } });
            actualizarUsuario(actualizado);
            setGuardado(true);
        } catch (err) {
            setError(err.message);
        } finally {
            setEnviando(false);
        }
    };

    return (
        <section className="panel perfil">
            <div className="perfil-cabecera">
                <Avatar id={usuario.id} nombre={nombre || usuario.nombre} avatar={avatar} tamano="enorme" />
                <div>
                    <h2 className="titulo-seccion">Mi perfil</h2>
                    <p className="detalle">@{usuario.username}{usuario.pisoNombre && ` · ${usuario.pisoNombre}`}</p>
                </div>
            </div>

            <form onSubmit={guardar}>
                <label>Nombre
                    <input value={nombre} maxLength={100} onChange={(e) => setNombre(e.target.value)} required />
                </label>

                <fieldset className="rejilla-avatares">
                    <legend>Avatar</legend>
                    <button type="button" className={avatar === '' ? 'opcion-avatar sin-avatar elegido' : 'opcion-avatar sin-avatar'}
                            aria-pressed={avatar === ''} onClick={() => setAvatar('')}>Iniciales</button>
                    {OPCIONES.map((emoji) => (
                        <button type="button" key={emoji} className={avatar === emoji ? 'opcion-avatar elegido' : 'opcion-avatar'}
                                aria-pressed={avatar === emoji} onClick={() => setAvatar(emoji)}>{emoji}</button>
                    ))}
                </fieldset>

                {error && <p className="error" role="alert">{error}</p>}
                {guardado && <p className="aviso-ok" role="status">Perfil guardado</p>}
                <button className="boton" disabled={enviando}>{enviando ? 'Guardando…' : 'Guardar cambios'}</button>
            </form>
        </section>
    );
}
