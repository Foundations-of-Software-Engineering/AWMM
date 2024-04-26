export class GameMap {
    canvas: HTMLCanvasElement;
    ctx: CanvasRenderingContext2D;
    layout: string[][];
    images: { [key: string]: HTMLImageElement } = {};
    imageNames: string[];
    tileSize: number;

    constructor(layout: string[][], imageNames: string[], tileSize: number) {
        this.canvas = document.getElementById("mapCanvas") as HTMLCanvasElement;
        this.ctx = this.canvas.getContext("2d")!;
        this.layout = layout;
        this.imageNames = imageNames;
        this.tileSize = tileSize;
        const numRows = this.layout.length;
        const numCols = this.layout[0].length;
        this.canvas.width = numCols * this.tileSize;
        this.canvas.height = numRows * this.tileSize;
    }

    // Function to load images
    async loadImages() {
        await Promise.all(this.imageNames.map(name => {
            return new Promise<void>((resolve, reject) => {
                const image = new Image();
                image.onload = () => {
                    this.images[name] = image;
                    resolve();
                };
                image.onerror = reject;
                image.src = `/images/rooms/${name}.png`; // Assuming images are in the "images" directory
            });
        }));
    }

    // Function to draw the map on the canvas
    drawMap() {
        const numRows = this.layout.length;
        const numCols = this.layout[0].length;

        console.log("NumRows:", numRows);
        console.log("NumCols:", numCols);

        for (let i = 0; i < numRows; i++) {
            for (let j = 0; j < numCols; j++) {
                const room = this.layout[i][j];
                const image = this.images[room];

                if (image) {
                    // Calculate the position of the image on the canvas
                    const x = j * this.tileSize;
                    const y = i * this.tileSize;

                    console.log("Room:", room);
                    console.log("Image:", image);
                    console.log("Position (x, y):", x, y);

                    // Draw the image on the canvas
                    this.ctx.drawImage(image, x, y, this.tileSize, this.tileSize);
                }
            }
        }
    }

}