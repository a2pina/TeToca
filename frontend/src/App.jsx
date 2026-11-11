import { useState } from 'react';
import { useSesion } from './context/SesionContext';
import Login from './components/Login';
import Registro from './components/Registro';
import Cabecera from './components/Cabecera';
import Navegacion from './components/Navegacion';
import Tablero from './components/Tablero';
import Convivientes from './components/Convivientes';
import Tareas from './components/Tareas';
import Ranking from './components/Ranking';
import Perfil from './components/Perfil';

export default function App() {
    const { usuario, cargando } = useSesion();
    const [pantalla, setPantalla] = useState('login');
    const [vista, setVista] = useState('tablero');

    if (cargando) return <p className="cargando">Cargando…</p>;

    if (!usuario) {
        return pantalla === 'login'
            ? <Login alRegistrarse={() => setPantalla('registro')} />
            : <Registro alVolver={() => setPantalla('login')} />;
    }

    if (!usuario.pisoId) {
        return (
            <>
                <Cabecera />
                <p className="contenido">Como administrador no perteneces a ningún piso. Gestiona la aplicación desde la API.</p>
            </>
        );
    }

    return (
        <>
            <Cabecera alPerfil={() => setVista('perfil')} />
            <div className="app">
                <Navegacion vista={vista} setVista={setVista} />
                <main className="principal">
                    {vista === 'tablero' && <Tablero />}
                    {vista === 'convivientes' && <Convivientes />}
                    {vista === 'tareas' && <div className="contenido"><Tareas pisoId={usuario.pisoId} /></div>}
                    {vista === 'ranking' && <div className="contenido"><Ranking pisoId={usuario.pisoId} usuarioId={usuario.id} /></div>}
                    {vista === 'perfil' && <div className="contenido"><Perfil /></div>}
                </main>
            </div>
        </>
    );
}
