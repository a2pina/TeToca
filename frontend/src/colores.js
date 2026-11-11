const COLORES = ['#2E7D6B', '#7C6CF0', '#D9722E', '#C2478B', '#2F7FD1', '#B08A1E'];

// Devuelve siempre el mismo color para el mismo conviviente
export const colorDe = (id) => COLORES[id % COLORES.length];