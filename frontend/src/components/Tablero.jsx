import { useCallback, useEffect, useState } from 'react';
import { api } from '../api/cliente';
import { useSesion } from '../context/SesionContext';
import TarjetaTarea from './TarjetaTarea';
import SinAsignar from './SinAsignar';
import Solicitudes from './Solicitudes';
import Calendario from './Calendario';
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
    const [convivientes, setConvivientes] = useState([]);
    const [error, setError] = useState('');
    const [aviso, setAviso] = useState(null);
    const [soloMias, setSoloMias] = useState(false);
    const [modo, setModo] = useState('estado');

    const cargar = useCallback(() => {
        api(`/pisos/${usuario.pisoId}/asignaciones`)
            .then((datos) => { setAsignaciones(datos); setError(''); })
            .catch((e) => setError(e.message));
    }, [usuario.pisoId]);

    useEffect(() => { cargar(); }, [cargar]);

    useEffect(() => {
        api(`/pisos/${usuario.pisoId}/convivientes`).then(setConvivientes).catch(() => {});
    }, [usuario.pisoId]);

    const mostrarAviso = (texto) => {
        setAviso(texto);
        setTimeout(() => setAviso(null), 3000);
    };

    const completar = async (id) => {
        setError('');
        try {
            const hecha = await api(`/asignaciones/${id}/completar`, { method: 'PATCH' });
            mostrarAviso(`🎉 ¡+${hecha.dificultad} punto${hecha.dificultad > 1 ? 's' : ''}! "${hecha.tarea}" completada`);
            cargar();
        } catch (e) {
            setError(e.message);
        }
    };

    const solicitar = async (id, convivienteId) => {
        setError('');
        try {
            const s = await api(`/asignaciones/${id}/solicitudes`, { method: 'POST', body: { convivienteId } });
            mostrarAviso(`Solicitud enviada a ${s.destinatario}`);
        } catch (e) {
            setError(e.message);
        }
    };

    const repartir = async () => {
        setError('');
        try {
            await api(`/pisos/${usuario.pisoId}/reparto`, { method: 'POST' });
            mostrarAviso('Tareas de la semana repartidas');
            cargar();
        } catch (e) {
            setError(e.message);
        }
    };

    const visibles = soloMias ? asignaciones.filter((a) => a.convivienteId === usuario.id) : asignaciones;
    const mias = asignaciones.filter((a) => a.convivienteId === usuario.id);
    const cuenta = (estado) => mias.filter((a) => a.estado === estado).length;
    const misVencidas = cuenta('VENCIDA');

    return (
        <div className="contenido">
            {misVencidas > 0 && (
                <p className="aviso-vencidas" role="alert">
                    ⚠ ⚠ Tienes {misVencidas} tarea{misVencidas > 1 ? 's' : ''} atrasada{misVencidas > 1 ? 's' : ''}
                </p>
            )}

            <div className="barra">
                <div className="semana">
                    <h2>{textoSemana()}</h2>
                    <div className="resumen">
                        <span className="chip">Te quedan {cuenta('PENDIENTE')}</span>
                        <span className="chip">Hechas {cuenta('COMPLETADA')}</span>
                        <span className="chip">Atrasadas {misVencidas}</span>
                    </div>
                </div>
                <div className="acciones">
                    <div className="selector-vista" role="group" aria-label="Vista del tablero">
                        <button className={modo === 'estado' ? 'boton' : 'boton boton-secundario'} onClick={() => setModo('estado')}>Por estado</button>
                        <button className={modo === 'calendario' ? 'boton' : 'boton boton-secundario'} onClick={() => setModo('calendario')}>Calendario</button>
                    </div>
                    <label className="interruptor">
                        <input type="checkbox" checked={soloMias} onChange={(e) => setSoloMias(e.target.checked)} />
                        Solo mis tareas
                    </label>
                    <button className="boton" onClick={repartir}>Repartir tareas de la semana</button>
                </div>
            </div>

            {error && <p className="error" role="alert">{error}</p>}

            <Solicitudes alResponder={cargar} />
            <SinAsignar pisoId={usuario.pisoId} asignaciones={asignaciones} alAsignar={cargar} />



            {modo === 'estado' ? (
                <section className="tablero">
                    {COLUMNAS.map((col) => {
                        const lista = visibles.filter((a) => a.estado === col.estado);
                        return (
                            <div key={col.estado} className={`columna columna-${col.estado.toLowerCase()}`}>
                                <h2>{col.titulo} <span className="contador">{lista.length}</span></h2>
                                {lista.length === 0 && <p className="columna-vacia">No hay tareas</p>}
                                {lista.map((a) => (
                                    <TarjetaTarea key={a.id} asignacion={a} esMia={a.convivienteId === usuario.id}
                                                  convivientes={convivientes} alCompletar={completar} alSolicitar={solicitar} />
                                ))}
                            </div>
                        );
                    })}
                </section>
            ) : (
                <Calendario asignaciones={visibles} usuarioId={usuario.id} convivientes={convivientes}
                            alCompletar={completar} alSolicitar={solicitar} />
            )}

            {aviso && <p className="toast" role="status">{aviso}</p>}
        </div>
    );
}