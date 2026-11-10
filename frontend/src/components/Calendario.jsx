import { useState } from 'react';
import TarjetaTarea from './TarjetaTarea';

const dos = (n) => String(n).padStart(2, '0');
const aTexto = (d) => `${d.getFullYear()}-${dos(d.getMonth() + 1)}-${dos(d.getDate())}`;

export default function Calendario({ asignaciones, usuarioId, convivientes, alCompletar, alSolicitar }) {
    const [desplazamiento, setDesplazamiento] = useState(0);

    const hoy = new Date();
    const lunes = new Date(hoy);
    lunes.setDate(hoy.getDate() - ((hoy.getDay() + 6) % 7) + desplazamiento * 7);
    const dias = Array.from({ length: 7 }, (_, i) => {
        const d = new Date(lunes);
        d.setDate(lunes.getDate() + i);
        return d;
    });
    const hoyTexto = aTexto(hoy);
    const f = (d) => d.toLocaleDateString('es-ES', { day: 'numeric', month: 'short' });

    return (
        <section className="calendario">
            <div className="calendario-nav">
                <button className="boton boton-secundario" onClick={() => setDesplazamiento(desplazamiento - 1)}>← Anterior</button>
                <strong>{f(dias[0])} – {f(dias[6])}</strong>
                <div className="botones-solicitud">
                    {desplazamiento !== 0 && <button className="boton boton-secundario" onClick={() => setDesplazamiento(0)}>Hoy</button>}
                    <button className="boton boton-secundario" onClick={() => setDesplazamiento(desplazamiento + 1)}>Siguiente →</button>
                </div>
            </div>

            <div className="calendario-dias">
                {dias.map((d) => {
                    const texto = aTexto(d);
                    const lista = asignaciones.filter((a) => a.fechaLimite === texto);
                    return (
                        <div key={texto} className={texto === hoyTexto ? 'dia dia-hoy' : 'dia'}>
                            <h3>
                                {d.toLocaleDateString('es-ES', { weekday: 'long' })}
                                <span>{d.getDate()}</span>
                            </h3>
                            {lista.length === 0 && <p className="columna-vacia">—</p>}
                            {lista.map((a) => (
                                <TarjetaTarea key={a.id} asignacion={a} esMia={a.convivienteId === usuarioId} mostrarEstado
                                              convivientes={convivientes} alCompletar={alCompletar} alSolicitar={alSolicitar} />
                            ))}
                        </div>
                    );
                })}
            </div>
        </section>
    );
}