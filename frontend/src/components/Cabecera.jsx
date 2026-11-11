import { useSesion } from '../context/SesionContext';
import Logo from './Logo';
import Avatar from './Avatar';

export default function Cabecera({ alPerfil }) {
    const { usuario, cerrarSesion } = useSesion();

    const datos = (
        <>
            <Avatar id={usuario.id} nombre={usuario.nombre} avatar={usuario.avatar} />
            <span>
                <strong>{usuario.nombre}</strong>
                {usuario.pisoNombre && <> · {usuario.pisoNombre}</>}
                {usuario.pisoCodigo && <> · Código: <strong>{usuario.pisoCodigo}</strong></>}
                {usuario.rol === 'ADMIN' && <> · Administrador</>}
            </span>
        </>
    );

    return (
        <header className="cabecera">
            <div className="logo">
                <Logo tamano={48} />
                <div>
                    <h1 className="marca">Te<span>Toca</span></h1>
                    <p className="eslogan">Se hace lo que se puede.</p>
                </div>
            </div>
            <div className="usuario">
                {alPerfil
                    ? <button className="boton-perfil" onClick={alPerfil} title="Mi perfil">{datos}</button>
                    : <span className="boton-perfil">{datos}</span>}
                <button className="boton boton-secundario" onClick={cerrarSesion}>Cerrar sesión</button>
            </div>
        </header>
    );
}
