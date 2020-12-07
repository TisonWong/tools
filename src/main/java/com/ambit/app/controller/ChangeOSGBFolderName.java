package com.ambit.app.controller;

/**
 * 背景
 *      下载的OSGB源数据文件夹命名不符合CityBuilder的规范, 将导致转换3DML失败
 *      因此需要将OSGB文件所在目录的文件夹重新命名(以OSGB文件的前缀为文件夹名称)
 *      例如:
 *          ..\OSGB\11-NE-12C\Data\11-NE-12C-1\Tile_+5593_+5472.osgb, Tile_+5593_+5472_L13_0.osgb....
 *          需要改成
 *          ..\OSGB\11-NE-12C\Data\Tile_+5593_+5472\Tile_+5593_+5472.osgb, Tile_+5593_+5472_L13_0.osgb....
 *
 */
public class ChangeOSGBFolderName {


}
