import { useCallback, useEffect, useState } from 'react';
import { api } from '../api/cliente';
import { useSesion } from '../context/SesionContext';
import TarjetaTarea from './TarjetaTarea';
import Ranking from './Ranking';
import Tareas from './Tareas';

const COLUMNAS = [
    { estado: 'PENDIENTE', titulo: 'Pendientes' },
    { estado: 'COMPLETADA', titulo: 'Completadas' },
    { estado: 'VENCIDA', titulo: 'Vencidas' },
];

function textoSemana() {
    const hoy = new Date();
    const lunes = new Date(hoy);
    lunes.setDate(hoy.getDate() - ((hoy.getDay() + 6) % 7));
    const domingo = new Date(lunes);
    domingo.setDate(lunes.getDate() + 6);
    const f = (d) => d.toLocaleDateString('es-ES', { day: 'numeric', month: 'short' });
    return `Semana del ${f(lunes)} al ${f(domingo)}`;
}

export default function Tablero() {
    const { usuario } = useSesion();
    const [asignaciones, setAsignaciones] = useState([]);
    const [error, setError] = useState('');
    const [refresco, setRefresco] = useState(0);
    const [soloMias, setSoloMias] = useState(false);

    const cargar = useCallback(() => {
        api(`/pisos/${usuario.pisoId}/asignaciones`)
            .then(setAsignaciones)
            .catch((e) => setError(e.message));
    }, [usuario.pisoId]);

    useEffect(() => { if (usuario.pisoId) cargar(); }, [cargar, usuario.pisoId]);

    const completar = async (id) => {
        setError('');
        try {
            await api(`/asignaciones/${id}/completar`, { method: 'PATCH' });
            cargar();
            setRefresco((r) => r + 1);
        } catch (e) {
            setError(e.message);
        }
    };

    const repartir = async () => {
        setError('');
        try {
            await api(`/pisos/${usuario.pisoId}/reparto`, { method: 'POST' });
            cargar();
        } catch (e) {
            setError(e.message);
        }
    };

    if (!usuario.pisoId) {
        return <p className="contenido">Como administrador no perteneces a ningún piso. Gestiona la aplicación desde la API.</p>;
    }

    const visibles = soloMias ? asignaciones.filter((a) => a.convivienteId === usuario.id) : asignaciones;
    const mias = asignaciones.filter((a) => a.convivienteId === usuario.id);
    const misPendientes = mias.filter((a) => a.estado === 'PENDIENTE').length;
    const misVencidas = mias.filter((a) => a.estado === 'VENCIDA').length;
    const misHechas = mias.filter((a) => a.estado === 'COMPLETADA').length;

    return (
        <div className="contenido">
            {misVencidas > 0 && (
                <p className="aviso-vencidas" role="alert">
                    ⚠ Tienes {misVencidas} tarea{misVencidas > 1 ? 's' : ''} atrasada{misVencidas > 1 ? 's' : ''}. ¡Al ogro no le gusta esperar! 👹
                </p>
            )}

            <div className="barra">
                <div className="semana">
                    <h2>{textoSemana()}</h2>
                    <div className="resumen">
                        <span className="chip">Te quedan {misPendientes}</span>
                        <span className="chip">Hechas {misHechas}</span>
                        <span className="chip">Atrasadas {misVencidas}</span>
                    </div>
                </div>
                <div className="acciones">
                    <label className="interruptor">
                        <input type="checkbox" checked={soloMias} onChange={(e) => setSoloMias(e.target.checked)} />
                        Solo mis tareas
                    </label>
                    <button className="boton" onClick={repartir}>Repartir tareas de la semana</button>
                </div>
            </div>

            {error && <p className="error" role="alert">{error}</p>}

            <section className="tablero">
                {COLUMNAS.map((col) => {
                    const lista = visibles.filter((a) => a.estado === col.estado);
                    return (
                        <div key={col.estado} className={`columna columna-${col.estado.toLowerCase()}`}>
                            <h2>{col.titulo} <span className="contador">{lista.length}</span></h2>
                            {lista.length === 0 && <p className="columna-vacia">No hay tareas</p>}
                            {lista.map((a) => (
                                <TarjetaTarea key={a.id} asignacion={a} esMia={a.convivienteId === usuario.id} alCompletar={completar} />
                            ))}
                        </div>
                    );
                })}
            </section>

            <div className="paneles">
                <Ranking pisoId={usuario.pisoId} usuarioId={usuario.id} refresco={refresco} />
                <Tareas pisoId={usuario.pisoId} />
            </div>
        </div>
    );
}