export default function Logo({ tamano = 48 }) {
    return (
        <svg width={tamano} height={tamano} viewBox="0 0 120 120" fill="none" aria-hidden="true">
            <path d="M48 38 L60 26 L72 38" stroke="#F2A541" strokeWidth="7" strokeLinecap="round" strokeLinejoin="round" />
            <path d="M60 34 V96 M42 96 H78 M22 46 H98" stroke="#E6E6EA" strokeWidth="7" strokeLinecap="round" />
            <path d="M22 46 L12 72 M22 46 L32 72 M98 46 L88 72 M98 46 L108 72" stroke="#E6E6EA" strokeWidth="4" strokeLinecap="round" />
            <path d="M9 72 Q22 88 35 72 Z M85 72 Q98 88 111 72 Z" fill="#3FB59A" stroke="#3FB59A" strokeWidth="4" strokeLinejoin="round" />
        </svg>
    );
}