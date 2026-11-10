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

const iniciales = (nombre) => nombre.trim().split(' ').map((p) => p[0]).join('').slice(0, 2).toUpperCase();
const ESTADOS = { PENDIENTE: 'Pendiente', COMPLETADA: 'Completada', VENCIDA: 'Vencida' };
const fechaCorta = (valor) => (valor.length === 10 ? aFecha(valor) : new Date(valor))
    .toLocaleDateString('es-ES', { weekday: 'short', day: 'numeric', month: 'numeric' });
export default function TarjetaTarea({ asignacion, esMia, convivientes = [], alCompletar, alSolicitar, mostrarEstado = false }) {
    const vencida = asignacion.estado === 'VENCIDA';
    const hecha = asignacion.estado === 'COMPLETADA';
    const nivel = '●'.repeat(asignacion.dificultad) + '○'.repeat(3 - asignacion.dificultad);
    const otros = convivientes.filter((c) => c.id !== asignacion.convivienteId);

    return (
        <article className={`tarjeta tarjeta-${asignacion.estado.toLowerCase()}`}>
            <div className="tarjeta-arriba">
                <h3>{asignacion.tarea}</h3>
                <span className="puntos-dificultad" aria-label={`Dificultad ${asignacion.dificultad} de 3`}>{nivel}</span>
            </div>

            <div className="persona">
                <span className="avatar" aria-hidden="true">{iniciales(asignacion.conviviente)}</span>
                <span>{asignacion.conviviente}{esMia && ' (tú)'}{vencida && ' 👹'}</span>
            </div>

            <p className={vencida ? 'cuando cuando-vencida' : 'cuando'}>
                {mostrarEstado && <span className={`estado estado-${asignacion.estado.toLowerCase()}`}>{ESTADOS[asignacion.estado]}</span>}
                {cuando(asignacion)}
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
        </article>
    );
}