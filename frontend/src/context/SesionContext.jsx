import { createContext, useContext, useEffect, useState } from 'react';
import { api, codificar, setCredenciales, setAlNoAutorizado } from '../api/cliente.js';

const SesionContext = createContext(null);
const CLAVE = 'tetoca.credenciales';

export function SesionProvider({ children }) {
    const [usuario, setUsuario] = useState(null);
    const [cargando, setCargando] = useState(true);

    const cerrarSesion = () => {
        sessionStorage.removeItem(CLAVE);
        setCredenciales(null);
        setUsuario(null);
    };

    useEffect(() => {
        setAlNoAutorizado(cerrarSesion);
        const guardadas = sessionStorage.getItem(CLAVE);
        if (!guardadas) { setCargando(false); return; }
        setCredenciales(guardadas);
        api('/auth/yo').then(setUsuario).catch(cerrarSesion).finally(() => setCargando(false));
    }, []);

    const iniciarSesion = async (username, password) => {
        const credenciales = codificar(username, password);
        setCredenciales(credenciales);
        try {
            const yo = await api('/auth/yo');
            sessionStorage.setItem(CLAVE, credenciales);
            setUsuario(yo);
        } catch {
            setCredenciales(null);
            throw new Error('Usuario o contraseña incorrectos');
        }
    };

    return (
        <SesionContext.Provider value={{ usuario, cargando, iniciarSesion, cerrarSesion }}>
            {children}
        </SesionContext.Provider>
    );
}

export const useSesion = () => useContext(SesionContext);