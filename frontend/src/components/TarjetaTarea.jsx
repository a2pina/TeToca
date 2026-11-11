import { useState } from 'react';
import Avatar from './Avatar';

const aFecha = (texto) => {
    const [a, m, d] = texto.split('-').map(Number);
    return new Date(a, m - 1, d);
};

function cuando(asignacion) {
    if (asignacion.estado === 'COMPLETADA') return 'Hecha';
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);
    const dias = Math.round((aFecha(asignacion.fechaLimite) - hoy) / 86400000);
    if (dias < 0) return `Atrasada ${-dias} día${dias === -1 ? '' : 's'}`;
    if (dias === 0) return 'Vence hoy';
    if (dias === 1) return 'Vence mañana';
    return `Vence en ${dias} días`;
}

const ESTADOS = { PENDIENTE: 'Pendiente', COMPLETADA: 'Completada', VENCIDA: 'Vencida' };
const fechaCorta = (valor) => (valor.length === 10 ? aFecha(valor) : new Date(valor))
    .toLocaleDateString('es-ES', { weekday: 'short', day: 'numeric', month: 'numeric' });

export default function TarjetaTarea({ asignacion, esMia, convivientes = [], alCompletar, alSolicitar, mostrarEstado = false }) {
    // Estado propio de la tarjeta: cerrada muestra lo mínimo, abierta muestra el detalle
    const [abierta, setAbierta] = useState(false);

    const vencida = asignacion.estado === 'VENCIDA';
    const hecha = asignacion.estado === 'COMPLETADA';
    const otros = convivientes.filter((c) => c.id !== asignacion.convivienteId);

    return (
        <article className={`tarjeta tarjeta-${asignacion.estado.toLowerCase()}`}>
            <button
                type="button"
                className="tarjeta-resumen"
                aria-expanded={abierta}
                onClick={() => setAbierta(!abierta)}
            >
                <span className="tarjeta-arriba">
                    <span className="tarjeta-titulo">{asignacion.tarea}</span>
                    <Avatar id={asignacion.convivienteId} nombre={asignacion.conviviente} avatar={asignacion.avatar} />
                </span>
                <span className="tarjeta-abajo">
                    <span className="puntos-dificultad" aria-label={`Dificultad ${asignacion.dificultad} de 3`}>
                        {[1, 2, 3].map((n) => <i key={n} className={n <= asignacion.dificultad ? 'punto punto-activo' : 'punto'} />)}
                    </span>
                    <span className={vencida ? 'cuando cuando-vencida' : 'cuando'}>{cuando(asignacion)}</span>
                </span>
            </button>

            {abierta && (
                <div className="tarjeta-detalle">
                    <p className="persona">
                        {asignacion.conviviente}{esMia && ' (tú)'}{vencida && ' 👹'}
                        {mostrarEstado && <span className={`estado estado-${asignacion.estado.toLowerCase()}`}>{ESTADOS[asignacion.estado]}</span>}
                    </p>

                    <dl className="fechas">
                        {asignacion.fechaAsignacion && <><dt>Asignada</dt><dd>{fechaCorta(asignacion.fechaAsignacion)}</dd></>}
                        <dt>Vence</dt><dd>{fechaCorta(asignacion.fechaLimite)}</dd>
                        {asignacion.fechaCompletada && <><dt>Hecha</dt><dd>{fechaCorta(asignacion.fechaCompletada)}</dd></>}
                    </dl>

                    {esMia && !hecha && (
                        <button className="boton" onClick={() => alCompletar(asignacion.id)}>Completar</button>
                    )}

                    {esMia && !hecha && otros.length > 0 && (
                        <label className="reasignar">Pedir cambio a
                            <select value="" onChange={(e) => alSolicitar(asignacion.id, Number(e.target.value))}>
                                <option value="">Elegir…</option>
                                {otros.map((c) => <option key={c.id} value={c.id}>{c.nombre}</option>)}
                            </select>
                        </label>
                    )}
                </div>
            )}
        </article>
    );
}
