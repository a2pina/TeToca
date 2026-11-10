const API = 'http://localhost:8080/api';

let credenciales = null;
let alNoAutorizado = null;

export function setCredenciales(valor) { credenciales = valor; }
export function setAlNoAutorizado(funcion) { alNoAutorizado = funcion; }

export function codificar(usuario, password) {
    const bytes = new TextEncoder().encode(`${usuario}:${password}`);
    let texto = '';
    bytes.forEach((b) => { texto += String.fromCharCode(b); });
    return btoa(texto);
}

export async function api(ruta, { method = 'GET', body } = {}) {
    const headers = { 'X-Requested-With': 'XMLHttpRequest' };
    if (credenciales) headers.Authorization = 'Basic ' + credenciales;
    if (body !== undefined) headers['Content-Type'] = 'application/json';

    const respuesta = await fetch(API + ruta, {
        method,
        headers,
        body: body !== undefined ? JSON.stringify(body) : undefined,
    });

    if (respuesta.status === 401 && credenciales && alNoAutorizado) alNoAutorizado();

    const datos = respuesta.status === 204 ? null : await respuesta.json().catch(() => null);
    if (!respuesta.ok) throw new Error(datos?.mensaje ?? 'Error ' + respuesta.status);
    return datos;
}