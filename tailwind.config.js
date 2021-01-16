const colors = require('tailwindcss/colors')

module.exports = {
    purge: [],
    darkMode: false, // or 'media' or 'class'
    theme: {
        extend: {
            maxHeight: {
                'screen-4/5': '80vh',
                'screen-70': '70vh'
            }
        },
        colors: colors,
        minWidth: {
            '0': '0',
            '1/4': '25%',
            '1/2': '50%',
            '3/4': '75%',
            '80': '80%',
            'full': '100%',
        },
        minHeight: {
            '0': '0',
            '1/4': '25%',
            '1/2': '50%',
            '3/4': '75%',
            '80': '80%',
            'full': '100%',
        }
    },
    variants: {
        extend: {},
    },
    plugins: [],
}
