import { useCallback, useEffect, useState } from 'react';
import { api } from '../api/cliente';

const NIVELES = { 1: 'Baja', 2: 'Media', 3: 'Alta' };
const VACIA = { nombre: '', dificultad: '1', frecuenciaSemanal: '1' };

export default function Tareas({ pisoId }) {
    const [tareas, setTareas] = useState([]);
    const [nueva, setNueva] = useState(VACIA);
    const [error, setError] = useState('');

    const cargar = useCallback(() => {
        api(`/pisos/${pisoId}/tareas`).then(setTareas).catch((e) => setError(e.message));
    }, [pisoId]);

    useEffect(() => { cargar(); }, [cargar]);

    const crear = async (e) => {
        e.preventDefault();
        setError('');
        try {
            await api(`/pisos/${pisoId}/tareas`, {
                method: 'POST',
                body: { nombre: nueva.nombre, dificultad: Number(nueva.dificultad), frecuenciaSemanal: Number(nueva.frecuenciaSemanal) },
            });
            setNueva(VACIA);
            cargar();
        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <section className="ranking">
            <h2>Tareas del piso</h2>
            <ol className="lista-tareas">
                {tareas.map((t) => (
                    <li key={t.id}>
                        <span>{t.nombre}</span>
                        <span className="detalle">{NIVELES[t.dificultad]} · {t.frecuenciaSemanal}/semana</span>
                    </li>
                ))}
            </ol>

            <form className="formulario-tarea" onSubmit={crear}>
                <h3>Añadir tarea</h3>
                <label>Nombre
                    <input value={nueva.nombre} onChange={(e) => setNueva({ ...nueva, nombre: e.target.value })} required />
                </label>
                <label>Dificultad
                    <select value={nueva.dificultad} onChange={(e) => setNueva({ ...nueva, dificultad: e.target.value })}>
                        <option value="1">Baja</option>
                        <option value="2">Media</option>
                        <option value="3">Alta</option>
                    </select>
                </label>
                <label>Veces por semana
                    <input type="number" min="1" max="21" value={nueva.frecuenciaSemanal}
                           onChange={(e) => setNueva({ ...nueva, frecuenciaSemanal: e.target.value })} required />
                </label>
                {error && <p className="error" role="alert">{error}</p>}
                <button className="boton">Añadir</button>
            </form>
        </section>
    );
}