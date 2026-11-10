import { useEffect, useState } from 'react';
import { api } from '../api/cliente';

export default function Ranking({ pisoId, usuarioId, refresco }) {
    const [filas, setFilas] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        api(`/pisos/${pisoId}/convivientes/ranking`).then(setFilas).catch((e) => setError(e.message));
    }, [pisoId, refresco]);

    return (
        <section className="ranking">
            <h2>Ranking del mes</h2>
            <p className="detalle">Convivientes del piso y sus puntos. Se reinicia cada mes.</p>
            {error && <p className="error">{error}</p>}
            <ol>
                {filas.map((f) => (
                    <li key={f.convivienteId}>
                        <span>{f.posicion}. {f.nombre}{f.convivienteId === usuarioId && ' (tú)'}</span>
                        <span>{f.puntos} pts</span>
                    </li>
                ))}
            </ol>
        </section>
    );
}