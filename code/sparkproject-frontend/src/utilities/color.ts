const COLORS = [
    '#D54062', '#206A5D', '#51ADCF', '#FCA652',
    '#EE6F57', '#150485', '#0F3460', '#9B99EF',
    '#FACA71', '#F4A285', '#EC7B94', '#D26CAB',
    '#a5ecd7', '#706BE3', '#40a8c4', '#fddb3a',
    '#696d7d', '#68b0ab', '#DB6400', '#ADE1BB',
    '#ea2c62', '#f5b461', '#9ddfd3', '#fae0df',
    '#9CC0FA', '#d4e09b', '#cbdfbd', '#a44a3f',
    '#ffd571', '#318fb5', '#bbd196', '#b52b65',
    '#53A1D5', '#5EC3E5', '#84DDE1', '#790C5A',
    '#FACA71', '#F4A285', '#EC7B94', '#D26CAB',
    '#DD96CE', '#E0BFEF', '#892cdc', '#6A097D',
];

const getColorDict = (nameSet: Set<any>) => {
    let colorDict = {};
    let counter = 0;
    nameSet.forEach((name, _) => {
        colorDict[name] = COLORS[counter % COLORS.length];
        counter ++;
    });
    return colorDict;
};
export {COLORS, getColorDict};