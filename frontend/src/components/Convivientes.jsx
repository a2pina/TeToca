import { useEffect, useState } from 'react';
import { api } from '../api/cliente';
import { useSesion } from '../context/SesionContext';
import Avatar from './Avatar';

export default function Convivientes() {
    const { usuario } = useSesion();
    const [lista, setLista] = useState([]);
    const [error, setError] = useState('');
    const [copiado, setCopiado] = useState(false);

    useEffect(() => {
        api(`/pisos/${usuario.pisoId}/convivientes`).then(setLista).catch((e) => setError(e.message));
    }, [usuario.pisoId]);

    const copiar = async () => {
        await navigator.clipboard.writeText(usuario.pisoCodigo);
        setCopiado(true);
        setTimeout(() => setCopiado(false), 2000);
    };

    return (
        <div className="contenido">
            <h2 className="titulo-seccion">{usuario.pisoNombre}</h2>

            <div className="invitar">
                <span>Código para invitar a tus convivientes:</span>
                <strong>{usuario.pisoCodigo}</strong>
                <button className="boton boton-secundario" onClick={copiar}>{copiado ? '¡Copiado!' : 'Copiar'}</button>
            </div>

            {error && <p className="error" role="alert">{error}</p>}

            <ul className="lista-convivientes">
                {lista.map((c) => (
                    <li key={c.id}>
                        <Avatar id={c.id} nombre={c.nombre} avatar={c.avatar} tamano="grande" />
                        <div>
                            <strong>{c.nombre}{c.id === usuario.id && ' (tú)'}</strong>
                            <p className="detalle">{c.email}</p>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
}
