export default function Logo({ tamano = 48 }) {
    return (
        <svg width={tamano} height={tamano} viewBox="0 0 120 120" fill="none" aria-hidden="true">
            <rect width="120" height="120" rx="28" fill="#1F2328" />
            <path d="M50 40 L60 30 L70 40" stroke="#F2A541" strokeWidth="8" strokeLinecap="round" strokeLinejoin="round" />
            <path d="M60 38 V92 M44 92 H76 M26 48 H94" stroke="#FFFFFF" strokeWidth="8" strokeLinecap="round" />
            <path d="M18 70 Q30 84 42 70 Z M78 70 Q90 84 102 70 Z" fill="#2E7D6B" stroke="#2E7D6B" strokeWidth="5" strokeLinejoin="round" />
        </svg>
    );
}