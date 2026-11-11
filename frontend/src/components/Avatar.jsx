import { colorDe } from '../colores';

const iniciales = (nombre) => nombre.trim().split(' ').map((p) => p[0]).join('').slice(0, 2).toUpperCase();

// Círculo de una persona: muestra su emoji si lo ha elegido y, si no, sus iniciales
export default function Avatar({ id, nombre, avatar, tamano = '' }) {
    const clases = ['avatar', tamano && `avatar-${tamano}`, avatar && 'avatar-emoji'].filter(Boolean).join(' ');
    return (
        <span className={clases} title={nombre} aria-hidden="true" style={{ background: colorDe(id) }}>
            {avatar || iniciales(nombre)}
        </span>
    );
}
