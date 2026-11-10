import { useState } from 'react';

export default function Navegacion({ vista, setVista }) {
    const [pisoAbierto, setPisoAbierto] = useState(true);

    const Item = ({ id, children }) => (
        <button className={vista === id ? 'nav-item activo' : 'nav-item'}
                aria-current={vista === id ? 'page' : undefined}
                onClick={() => setVista(id)}>
            {children}
        </button>
    );

    return (
        <nav className="navegacion" aria-label="Secciones">
            <Item id="tablero">📋 Tablero de la semana</Item>

            <button className="nav-item" aria-expanded={pisoAbierto} onClick={() => setPisoAbierto(!pisoAbierto)}>
                🏠 Piso {pisoAbierto ? '▾' : '▸'}
            </button>
            {pisoAbierto && (
                <div className="nav-grupo">
                    <Item id="convivientes">👥 Convivientes</Item>
                </div>
            )}

            <Item id="tareas">🧹 Tareas del piso</Item>
            <Item id="ranking">🏆 Ranking</Item>
        </nav>
    );
}