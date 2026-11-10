import { useEffect, useState } from 'react';
import { api } from '../api/cliente';

const dos = (n) => String(n).padStart(2, '0');
const aTexto = (d) => `${d.getFullYear()}-${dos(d.getMonth() + 1)}-${dos(d.getDate())}`;

function limitesSemana() {
    const hoy = new Date();
    const lunes = new Date(hoy);
    lunes.setDate(hoy.getDate() - ((hoy.getDay() + 6) % 7));
    const domingo = new Date(lunes);
    domingo.setDate(lunes.getDate() + 6);
    return { lunes: aTexto(lunes), domingo: aTexto(domingo) };
}

export default function SinAsignar({ pisoId, asignaciones, refresco, alAsignar }) {
    const [tareas, setTareas] = useState([]);
    const [convivientes, setConvivientes] = useState([]);
    const [elegidos, setElegidos] = useState({});
    const [fechas, setFechas] = useState({});
    const [error, setError] = useState('');
    const { lunes, domingo } = limitesSemana();

    useEffect(() => {
        api(`/pisos/${pisoId}/tareas`).then(setTareas).catch((e) => setError(e.message));
        api(`/pisos/${pisoId}/convivientes`).then(setConvivientes).catch((e) => setError(e.message));
    }, [pisoId, refresco]);

    // Asignada = tiene una asignación sin completar, o completada esta semana
    const sinAsignar = tareas.filter((t) => !asignaciones.some(
        (a) => a.tareaId === t.id && (a.estado !== 'COMPLETADA' || a.fechaLimite >= lunes),
    ));

    const asignar = async (tareaId) => {
        setError('');
        const convivienteId = elegidos[tareaId];
        if (!convivienteId) { setError('Elige a quién asignar la tarea'); return; }
        try {
            await api(`/tareas/${tareaId}/asignaciones`, {
                method: 'POST',
                body: { convivienteId: Number(convivienteId), fechaLimite: fechas[tareaId] ?? domingo },
            });
            alAsignar();
        } catch (e) {
            setError(e.message);
        }
    };

    if (sinAsignar.length === 0) return null;

    return (
        <section className="sin-asignar">
            <h2>Sin asignar <span className="contador">{sinAsignar.length}</span></h2>
            {error && <p className="error" role="alert">{error}</p>}
            <ul>
                {sinAsignar.map((t) => (
                    <li key={t.id}>
                        <span className="nombre-tarea">{t.nombre}</span>
                        <label className="visualmente-oculto" htmlFor={`quien-${t.id}`}>Asignar {t.nombre} a</label>
                        <select id={`quien-${t.id}`} value={elegidos[t.id] ?? ''}
                                onChange={(e) => setElegidos({ ...elegidos, [t.id]: e.target.value })}>
                            <option value="">¿A quién?</option>
                            {convivientes.map((c) => <option key={c.id} value={c.id}>{c.nombre}</option>)}
                        </select>
                        <label className="visualmente-oculto" htmlFor={`fecha-${t.id}`}>Fecha límite de {t.nombre}</label>
                        <input id={`fecha-${t.id}`} type="date" value={fechas[t.id] ?? domingo}
                               onChange={(e) => setFechas({ ...fechas, [t.id]: e.target.value })} />
                        <button className="boton" onClick={() => asignar(t.id)}>Asignar</button>
                    </li>
                ))}
            </ul>
        </section>
    );
}