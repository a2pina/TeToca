import { useCallback, useEffect, useState } from 'react';
import { api } from '../api/cliente';

export default function Solicitudes({ alResponder }) {
    const [solicitudes, setSolicitudes] = useState([]);
    const [error, setError] = useState('');

    const cargar = useCallback(() => {
        api('/solicitudes/recibidas').then(setSolicitudes).catch((e) => setError(e.message));
    }, []);

    useEffect(() => { cargar(); }, [cargar]);

    const responder = async (id, accion) => {
        setError('');
        try {
            await api(`/solicitudes/${id}/${accion}`, { method: 'PATCH' });
            cargar();
            alResponder();
        } catch (e) {
            setError(e.message);
        }
    };

    if (solicitudes.length === 0) return null;

    return (
        <section className="solicitudes">
            <h2>Te piden un cambio <span className="contador">{solicitudes.length}</span></h2>
            {error && <p className="error" role="alert">{error}</p>}
            <ul>
                {solicitudes.map((s) => (
                    <li key={s.id}>
            <span>
              <strong>{s.solicitante}</strong> te pide que hagas <strong>{s.tarea}</strong>
                {' '}(dificultad {s.dificultad})
            </span>
                        <div className="botones-solicitud">
                            <button className="boton" onClick={() => responder(s.id, 'aceptar')}>Aceptar</button>
                            <button className="boton boton-secundario" onClick={() => responder(s.id, 'rechazar')}>Rechazar</button>
                        </div>
                    </li>
                ))}
            </ul>
        </section>
    );
}