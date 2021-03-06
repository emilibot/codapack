# CoDaPack

Historically CoDaPack 3D was intended to be a package of Compositional 
Data with an easy and intuitive way of use. For this reason from the 
beginning it has been associated with Excel, software known and used 
for many people. However, over the years different versions of Excel 
and Windows have been appeared and CoDaPack has had to be adapted to 
these new versions due to some incompatibilities.

For this reason and also because of CoDaPack only worked with Excel under 
windows; the Girona Compositional Data Group decided to implement a new 
software with at least the same capabilities and the same profile of users 
but independent of any other software.

The new CoDaPack has three different areas: the variables area, the data 
area and the results area which has a textual output window and independent 
graphical output. Also it is expected to work at least under Unix, Window 
and MacOS operating systems.

# CoDaPack: start developing

Clone codapack (and optionally codapack-updater)

```{r}
git clone http://github.com/mcomas/codapack.git
git clone http://github.com/mcomas/codapack-updater.git
```

Download external libraries.

```{r}
curl mcomas.net/codapack/codapack-lib.zip > codapack-lib.zip
```

and decompress them into `codapack/libs` folder

```{r}
unzip codapack-lib.zip -d codapack/lib/
```
