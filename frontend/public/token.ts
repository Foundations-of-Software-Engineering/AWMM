export class Tokens {
    canvas: HTMLCanvasElement;
    ctx: CanvasRenderingContext2D;
    mapHeight: number;
    mapWidth: number;
    tileSize: number;
    characterColors: {[key: string]: string } = {
        "Scarlet": "red",
        "Mustard": "yellow",
        "White": "white",
        "Green": "green",
        "Peacock": "blue",
        "Plum": "plum"
    }
    characterOffset: {[key: string]: number} = {
        "Scarlet": 0,
        "Mustard": 1,
        "White": 2,
        "Green": 3,
        "Peacock": 4,
        "Plum": 5
    }
    tokenPositions: {[key: string]: {row: number, col: number}} = {};

    constructor(canvas: HTMLCanvasElement, layout: string[][], tileSize: number) {
        this.canvas = canvas;
        this.ctx = this.canvas.getContext("2d")!;
        this.tileSize = tileSize;
        this.mapHeight = layout.length * tileSize;
        this.mapWidth = layout[0].length * tileSize;
    }

    public updatePostion(name: string, row: number, col: number) {
        const buffer = 10;
        const offsetX = this.tileSize / 3 * (this.characterOffset[name] % 2) + buffer;
        const offsetY = this.tileSize / 3 * Math.floor(this.characterOffset[name] / 2) + buffer;

        col = col * this.tileSize + offsetX;
        row = row * this.tileSize + offsetY;

        this.tokenPositions[name] = {row, col};
    }

    public drawTokens(){
        for (let name in this.tokenPositions){
            let row = this.tokenPositions[name].row;
            let col = this.tokenPositions[name].col;
            let tokenColor = this.characterColors[name];
            this.drawToken(tokenColor, col, row);
        }
    }
    private drawToken(tokenColor: string, x: number, y: number) {
        const tokenSize = 50; // Adjust the size of the square token as needed

        this.ctx.fillStyle = tokenColor;
        this.ctx.fillRect(x, y, tokenSize, tokenSize);
    }
}