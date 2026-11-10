import { useState } from 'react';
import { useSesion } from './context/SesionContext';
import Login from './components/Login';
import Registro from './components/Registro';
import Cabecera from './components/Cabecera';
import Tablero from './components/Tablero';

export default function App() {
  const { usuario, cargando } = useSesion();
  const [pantalla, setPantalla] = useState('login');

  if (cargando) return <p className="cargando">Cargando…</p>;

  if (!usuario) {
    return pantalla === 'login'
        ? <Login alRegistrarse={() => setPantalla('registro')} />
        : <Registro alVolver={() => setPantalla('login')} />;
  }

  return (
      <>
        <Cabecera />
        <Tablero />
      </>
  );
}