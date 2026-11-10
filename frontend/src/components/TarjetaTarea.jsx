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

const iniciales = (nombre) => nombre.split(' ').map((p) => p[0]).join('').slice(0, 2).toUpperCase();

export default function TarjetaTarea({ asignacion, esMia, alCompletar }) {
    const vencida = asignacion.estado === 'VENCIDA';
    const puedeCompletar = esMia && asignacion.estado !== 'COMPLETADA';
    const nivel = '●'.repeat(asignacion.dificultad) + '○'.repeat(3 - asignacion.dificultad);

    return (
        <article className={vencida ? 'tarjeta tarjeta-vencida' : 'tarjeta'}>
            <div className="tarjeta-arriba">
                <h3>{asignacion.tarea}</h3>
                <span className="puntos-dificultad" aria-label={`Dificultad ${asignacion.dificultad} de 3`}>{nivel}</span>
            </div>
            <div className="persona">
                <span className="avatar" aria-hidden="true">{iniciales(asignacion.conviviente)}</span>
                <span>{asignacion.conviviente}{esMia && ' (tú)'}{vencida && ' 👹'}</span>
            </div>
            <p className={vencida ? 'cuando cuando-vencida' : 'cuando'}>{cuando(asignacion)}</p>
            {puedeCompletar && (
                <button className="boton" onClick={() => alCompletar(asignacion.id)}>Completar</button>
            )}
        </article>
    );
}