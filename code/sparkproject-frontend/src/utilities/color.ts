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
    '#FF0585', '#F733BC', '#534A23', '#CAB74B',
    '#E7D332', '#7EEB93', '#4198AA', '#F29EF1',
    '#7E812C', '#E9F732', '#1EF79B', '#49DC87',
    '#01C1C9', '#0024E5', '#17554F', '#40D7A9',
    '#058C20', '#D47487', '#E34164', '#CA921D',
    '#7A1FDE', '#115358', '#9D36FA', '#AD2F59',
    '#1024E4', '#537B52', '#24DD2A', '#351693',
    '#06EFE5', '#67E679', '#132DFB', '#5A50E5',
    '#4C5D63', '#7D8B73', '#487CD4', '#6889D3',
    '#350BDB', '#0D4759', '#A47930', '#A20815',
    '#E2DE73', '#6515A7', '#97E8F9', '#8D5BAC',
    '#0CCC5F', '#569CF8', '#6A5349', '#DF7CA9',
    '#916426', '#93C882', '#561A67', '#89AFE4',
    '#237EB9', '#E95DEF', '#9E9B93', '#FE3E1B',
    '#A24D42', '#FD85C2', '#9ACAAE', '#F16259',
    '#843BC0', '#9C454B', '#6A79C1', '#9A8EB1',
    '#AEFBF1', '#E784C9', '#5DC9E7', '#419EB6',
    '#5A53FC', '#C3D116', '#7C59D6', '#A13600',
    '#C4D18D', '#F31B56', '#A37817', '#1F152B',
    '#820DCE', '#61C63A', '#C5683C', '#1DC1AE',
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