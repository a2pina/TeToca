import { useEffect, useState } from 'react';
import { api } from '../api/cliente';

const MEDALLAS = ['🥇', '🥈', '🥉'];

export default function Ranking({ pisoId, usuarioId, refresco }) {
    const [filas, setFilas] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        api(`/pisos/${pisoId}/convivientes/ranking`).then(setFilas).catch((e) => setError(e.message));
    }, [pisoId, refresco]);

    const maximo = Math.max(1, ...filas.map((f) => f.puntos));
    const mes = new Date().toLocaleDateString('es-ES', { month: 'long' });

    return (
        <section className="ranking">
            <h2 className="titulo-seccion">Ranking de {mes}</h2>
            <p className="detalle">Cada tarea completada suma su dificultad. Se reinicia el día 1 de cada mes.</p>
            {error && <p className="error">{error}</p>}
            <ol>
                {filas.map((f) => (
                    <li key={f.convivienteId} className={f.convivienteId === usuarioId ? 'fila-ranking yo' : 'fila-ranking'}>
            <span className="posicion" aria-label={`Posición ${f.posicion}`}>
              {f.puntos > 0 && f.posicion <= 3 ? MEDALLAS[f.posicion - 1] : f.posicion}
            </span>
                        <div className="fila-datos">
                            <div className="fila-nombre">
                                <span>{f.nombre}{f.convivienteId === usuarioId && ' (tú)'}</span>
                                <strong>{f.puntos} pts</strong>
                            </div>
                            <div className="barra-puntos" aria-hidden="true">
                                <div style={{ width: `${(f.puntos / maximo) * 100}%` }} />
                            </div>
                        </div>
                    </li>
                ))}
            </ol>
        </section>
    );
}