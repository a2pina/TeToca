import { useSesion } from '../context/SesionContext';

export default function Cabecera() {
    const { usuario, cerrarSesion } = useSesion();

    return (
        <header className="cabecera">
            <div>
                <h1 className="marca">Te<span>Toca</span></h1>
                <p className="eslogan">Se hace lo que se puede.</p>
            </div>
            <div className="usuario">
        <span>
          <strong>{usuario.nombre}</strong>
            {usuario.pisoNombre && <> · {usuario.pisoNombre}</>}
            {usuario.pisoCodigo && <> · Código: <strong>{usuario.pisoCodigo}</strong></>}
            {usuario.rol === 'ADMIN' && <> · Administrador</>}
        </span>
                <button className="boton boton-secundario" onClick={cerrarSesion}>Cerrar sesión</button>
            </div>
        </header>
    );
}