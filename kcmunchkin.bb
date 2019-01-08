AMIGA
WBStartup

.variables
DEFTYPE .w
Dim map(9,7)
xoff.b=14+16
yoff.b=24+16
pillspeed.f
pillspeedinc.f
pilllast
speed.f
gfx=1
gamestate=4
Dim highscore(8)

NEWTYPE .sprite
x.f
y.f
xs.f
ys.f
oldx
oldy
xdest
ydest
spd.q
subpix.q
frame.w
state.w
direction.w
col.q ;colour for pills, timers for enemies
End NEWTYPE

DEFTYPE .sprite player
Dim enemy.sprite(3)
Dim pill.sprite(12)


.displaysetup
;COPPER1.l=$6 ;bitplanes
;COPPER+$20 ;dual playfield
;InitCopList 1,44,DispHeight,COPPER1,8,32,0
;create 16 colour display for game
COPPER0.l=$4 ;bitplanes
;COPPER0+$20 ;dual playfield
;COPPER0+$400
InitCopList 0,44,DispHeight,COPPER0,8,32,0

.ld_loadingscreen
AMIGA
BitMap 1,320,256,4
Use BitMap 1
Cls
LoadBitMap 1,"amigathon16.iff"
LoadPalette 1,"amigathon16.iff"
BLITZ
CreateDisplay 0
DisplayBitMap 0,1
DisplayPalette 0,1

.ld_background
QAMIGA
BitMap 0,320,256,4
LoadBitMap 0,"background.iff"
LoadPalette 0,"background.iff"
GetaShape 0,32,32,32,32 ; blank background square
;1 sided squares
GetaShape 4,0,0,32,32 ; get first square (straight linge top)
CopyShape 4,8:CopyShape 4,2:CopyShape 4,1
Rotate 1,0.25:Rotate 2,0.5:Rotate 8,0.75
;2 sided squares
GetaShape 6,32,0,32,32:CopyShape 6,9
Rotate 9,0.25
GetaShape 12,64,0,32,32:CopyShape 12,5:CopyShape 12,10:CopyShape 12,3
Rotate 5,0.25:Rotate 10,0.75:Rotate 3,0.5
;3 sided squares
GetaShape 14,96,0,32,32:CopyShape 14,11:CopyShape 14,13:CopyShape 14,7
Rotate 11,0.75:Rotate 13,0.25:Rotate 7,0.5
;4 sided square
GetaShape 15,128,0,32,32
For i=0To15:Handle i,0,0:Next i
Handle 7,1,1:Handle 5,1,0:Handle 8,0,1:Handle 1,1,0:Handle 10,0,1
Handle 2,1,1:Handle 3,1,1:Handle 13,1,0:Handle 11,0,1:Handle 9,1,0

.ld_sounds
LoadSound 0,"sound0.iff" ;running 1
LoadSound 1,"sound1.iff" ;idle 1
LoadSound 2,"sound2.iff" ;running 2
LoadSound 3,"sound3.iff" ;pill eaten
LoadSound 4,"sound4.iff" ;powerpill eaten
LoadSound 5,"sound5.iff" ;ghost eaten
LoadSound 6,"sound6.iff" ;level complete
LoadSound 7,"sound7.iff" ;idle chasing
LoadSound 8,"sound8.iff" ;dead

.ld_sprites
BitMap 1,320,256,4
For m=0 To 1
If m=0
  LoadBitMap 0,"gfx7800.iff"
  LoadPalette 0,"gfx7800.iff"
  LoadPalette 0,"gfx7800.iff",16
  gfx=0
  Else
  LoadBitMap 0,"gfx7000.iff"
  LoadPalette 0,"gfx7000.iff"
  LoadPalette 0,"gfx7000.iff",16
  gfx=81
EndIf

Use BitMap 0
;PlayerStill
GetaShape 16+gfx,7,2,16,16
;PlayerUp
GetaShape 17+gfx,7,83,16,16
GetaShape 18+gfx,33,83,16,16
GetaShape 19+gfx,59,83,16,16
GetaShape 20+gfx,85,83,16,16
;PlayerDown
GetaShape 21+gfx,7,110,16,16
GetaShape 22+gfx,33,110,16,16
GetaShape 23+gfx,59,110,16,16
GetaShape 24+gfx,85,110,16,16
;playerleft
GetaShape 25+gfx,7,29,16,16
GetaShape 26+gfx,33,29,16,16
GetaShape 27+gfx,59,29,16,16
GetaShape 28+gfx,85,29,16,16
;playerright
GetaShape 29+gfx,7,56,16,16
GetaShape 30+gfx,33,56,16,16
GetaShape 31+gfx,59,56,16,16
GetaShape 32+gfx,85,56,16,16


;enemy1up
GetaShape 33+gfx,214,29,16,16
GetaShape 34+gfx,240,29,16,16
;enemy2up
GetaShape 39+gfx,214,57,16,16
GetaShape 40+gfx,240,57,16,16
;enemy3up
GetaShape 37+gfx,214,84,16,16
GetaShape 38+gfx,240,84,16,16
;enemy4up
GetaShape 35+gfx,214,110,16,16
GetaShape 36+gfx,240,110,16,16
;enemy1down
GetaShape 41+gfx,266,29,16,16
GetaShape 42+gfx,292,29,16,16
;enemy2down
GetaShape 47+gfx,266,57,16,16
GetaShape 48+gfx,292,57,16,16
;enemy3down
GetaShape 45+gfx,266,84,16,16
GetaShape 46+gfx,292,84,16,16
;enemy4down
GetaShape 43+gfx,266,110,16,16
GetaShape 44+gfx,292,110,16,16
;enemy1left
GetaShape 49+gfx,110,29,16,16
GetaShape 50+gfx,136,29,16,16
;enemy2left
GetaShape 55+gfx,110,57,16,16
GetaShape 56+gfx,136,57,16,16
;enemy3left
GetaShape 53+gfx,110,84,16,16
GetaShape 54+gfx,136,84,16,16
;enemy4left
GetaShape 51+gfx,110,110,16,16
GetaShape 52+gfx,136,110,16,16
;enemy1right
GetaShape 57+gfx,162,29,16,16
GetaShape 58+gfx,188,29,16,16
;enemy2right
GetaShape 63+gfx,162,57,16,16
GetaShape 64+gfx,188,57,16,16
;enemy3right
GetaShape 61+gfx,162,84,16,16
GetaShape 62+gfx,188,84,16,16
;enemy4right
GetaShape 59+gfx,162,110,16,16
GetaShape 60+gfx,188,110,16,16
;ghost
GetaShape 65+gfx,33,191,16,16
GetaShape 66+gfx,59,191,16,16
GetaShape 67+gfx,85,191,16,16
GetaShape 68+gfx,111,191,16,16
GetaShape 69+gfx,137,191,16,16
GetaShape 70+gfx,163,191,16,16
GetaShape 71+gfx,189,191,16,16
;pill
GetaShape 72+gfx,63,144,6,4
GetaShape 73+gfx,89,144,6,4
;playerdead
GetaShape 74+gfx,7,164,16,16
GetaShape 75+gfx,33,164,16,16
GetaShape 76+gfx,59,164,16,16
GetaShape 77+gfx,85,164,16,16
GetaShape 78+gfx,111,164,16,16
GetaShape 79+gfx,137,164,16,16
GetaShape 80+gfx,163,164,16,16
GetaShape 81+gfx,189,164,16,16
;blankpill
GetaShape 82+gfx,0,0,6,4
;midhandle every shape
For i=16+gfx To 82+gfx:MidHandle i:Next i
;change shapes to sprites
For i = 16+gfx To 71+gfx:GetaSprite i,i:Free Shape i:Next i
For i = 74+gfx To 81+gfx:GetaSprite i,i:Free Shape i:Next i
Next m
gfx=0

.ld_titlescreen
Use BitMap 1
LoadPalette 1,"title.iff"
DisplayPalette 0,1
Cls
LoadBitMap 1,"title.iff"
LoadPalette 0,"background.iff" ;change background pallete back


.mainloop
While Joyb(0)=0
  If gamestate=0 ;setup game
    Restore
    Gosub init_newgame
    Gosub level
    Gosub draw_level
    gamestate=1
  EndIf

  If gamestate=1 ;run game standard
    Gosub playerupdate
    Gosub counters
    For e=0 To 2 ;3 enemies to update
    Gosub enemyupdate
    Next e
    Gosub pillupdate
    Gosub pillcollide
    p+1:p=QWrap(p,0,12) ;select pill to update next time
    If pillseaten=12
      gamestate=3 ;goto level complete
      player\col=0
      player\frame=74
      i=0
      Sound 6,%0101
    EndIf
    VWait
  EndIf

  If gamestate=2 ; game over
    Gosub playerdead
    Gosub pillupdate
    p+1:p=QWrap(p,0,12)
    VWait
  EndIf

  If gamestate=3 ; level complete
    Gosub levelfinish
    VWait
  EndIf

  If gamestate=4 ; title screen
    Gosub titlescreen
  EndIf

  Gosub gfxswitch
  Wend
End



.init_newgame
BLITZ
DisplayBitMap 0,0
DisplayPalette 0,0
Use BitMap 0
Cls

Let player\x=4
Let player\y=3
Let player\xs=0
Let player\ys=0
Let player\oldx=0
Let player\oldy=0
Let player\spd=1.2
Let player\subpix=0
Let player\direction=0
Let player\col=0

For e=0 To 3
Let enemy(e)\x=4
Let enemy(e)\y=4
Let enemy(e)\xs=0
Let enemy(e)\ys=0
Let enemy(e)\oldx=0
Let enemy(e)\oldy=0
Let enemy(e)\spd=1.2
Let enemy(e)\subpix=0
Let enemy(e)\state=1
Let enemy(e)\direction=0
Let enemy(e)\col=0
Next e

For p=0 To 11
Let pill(p)\spd=pillspeed
Let pill(p)\state=1
Let pill(p)\xs=0
Let pill(p)\ys=0
Let pill(p)\oldx=0
Let pill(p)\oldy=0
Let pill(p)\subpix=0
Let pill(p)\direction=0
Let pill(p)\col=0
Next p
p=0
c1=0:c2=0:c3=0:c4=0
pillseaten=0
spinner=0

;position pills
pill(0)\x=1:pill(0)\y=0
pill(1)\x=0:pill(1)\y=1
pill(2)\x=7:pill(2)\y=0
pill(3)\x=8:pill(3)\y=1
pill(4)\x=0:pill(4)\y=5
pill(5)\x=1:pill(5)\y=6
pill(6)\x=7:pill(6)\y=6
pill(7)\x=8:pill(7)\y=5
pill(8)\x=0:pill(8)\y=0:pill(8)\state=2
pill(9)\x=8:pill(9)\y=0:pill(9)\state=2
pill(10)\x=0:pill(10)\y=6:pill(10)\state=2
pill(11)\x=8:pill(11)\y=6:pill(11)\state=2
Return

.draw_level
Use BitMap 0
Cls
;pick map
If lmap=0 Then Restore map0
If lmap=1 Then Restore map1
If lmap=2 Then Restore map2
If lmap=3 Then Restore map3
;blit the map out
For row=0 To 6
  For column=0 To 8
  Read map
  Let map(column,row)=map
  Blit map(column,row),column*32+xoff-16,row*32+yoff-16
  Next column
Next row
;draw outer edges
Boxf 14,20,301,26,1
Boxf 14,245,301,251,1
Boxf 10,20,16,251,1
Boxf 299,20,305,251,1
Boxf 10,155,16,180,0
Boxf 299,155,305,180,0
;draw hud
BitMapOutput 0
Colour 10
Locate 2,1
Print "Score"
Locate 13,1
Print "Level"
Locate 23,1
Print "Highscore"
Colour 7
Locate 19,1
Print level
Gosub hud
Return



.playerupdate
;decide what to do every 32 frames
If player\subpix=0
  player\xs=0:player\ys=0
  If player\direction=4 Then player\y-1
  If player\direction=2 Then player\y+1
  If player\direction=8 Then player\x-1
  If player\direction=1 Then player\x+1
  player\x=QWrap(player\x,0,9)
  Let exit=map(player\x,player\y)
  Gosub canigo
  player\direction=0
  player\frame=16
  If Joyr(1)=0 AND uok=1 Then player\direction=4
  If Joyr(1)=4 AND dok=1 Then player\direction=2
  If Joyr(1)=6 AND lok=1 Then player\direction=8
  If Joyr(1)=2 AND rok=1 Then player\direction=1
EndIf

;move player in desired direction and set graphic
If player\direction=4 Then player\ys-player\spd:player\frame=17
If player\direction=2 Then player\ys+player\spd:player\frame=21
If player\direction=8 Then player\xs-player\spd:player\frame=25
If player\direction=1 Then player\xs+player\spd:player\frame=29

;play appropriate sound and blackout level if required
If player\direction>0
  player\subpix+player\spd
  If c3=0 Then Sound 0,%1000
  If c3=5 Then Sound 2,%0010
  If flash=1 Then PalRGB 0,5,0,0,0:DisplayPalette 0,0
Else
  If c3=0 AND enemy(0)\state>1 Then Sound 7,%1010
  If c3=0 AND enemy(0)\state<2 Then Sound 1,%1010
  If flash=1 Then PalRGB 0,5,15,0,15:DisplayPalette 0,0
EndIf

;we move 32 pixels before we make a new direction desicion
If player\subpix >=32 Then player\subpix=0

;animation depending on how far thru the 32 pixels we are
If player\subpix>7 Then player\frame+1
If player\subpix>15 Then player\frame+1
If player\subpix>23 Then player\frame+1

;display the player sprite
DisplaySprite 0,player\frame+gfx,player\x*32+player\xs+xoff,player\y*32+player\ys+yoff,0
Return


.playerdead
;turn off any maze flash
If flash=1 Then PalRGB 0,5,15,0,15:DisplayPalette 0,0

;use the col var as a timer to control death animation
player\col+1:player\col=QWrap(player\col,0,4)
If player\col=0
  player\frame+1
EndIf

;clear the enemies from the screen on death (we dont need to do it every frame)
If player\frame=75
  For e=0 To 2:DisplaySprite 0,16,340,0,e*2+2:Next e
EndIf

;display the player dying and remove from screen when finished
If player\frame<82
  DisplaySprite 0,player\frame+gfx,player\x*32+player\xs+xoff,player\y*32+player\ys+yoff,0
Else
  DisplaySprite 0,16,340,0,0 ;remove from screen
EndIf

;keep using frame as a timer then eventually reset the game
If player\frame=140
  gamestate=4
  level=0
  score=0
EndIf
Return

.pillcollide
; check if pills collide, its too slow to check everyone so check them over 3 frames
If c2=0 Then ia=0:ib=3
If c2=1 Then ia=4:ib=7
If c2=2 Then ia=8:ib=11

For i = ia To ib
If RectsHit(pill(i)\x*32+pill(i)\xs,pill(i)\y*32+pill(i)\ys,8,8,player\x*32+player\xs,player\y*32+player\ys,16,16)
  ;check for normal pill
  If pill(i)\state=1 ;checks if the pill is normal
    score+1:Gosub hud
    pillseaten+1
    pillspeed+pillspeedinc ;speed up the remaining pills
    pill(i)\state=0 ;disable pill
    Sound 3,%0101
    ;blankout pill
    Boxf pill(i)\oldx,pill(i)\oldy,pill(i)\oldx+8,pill(i)\oldy+4,0
  EndIf

  ;check for powerpill
  If pill(i)\state=2; checks if the pill is powerpill
    score+3:Gosub hud
    pillseaten+1
    pillspeed+pillspeedinc ;speed up the remaining pills
    pill(i)\state=0 ;disable pill
    Sound 4,%0101
    ;put enemies into runaway state
    For j=0 To 2
      If enemy(j)\state=1 OR enemy(j)\state=2
      Let enemy(j)\state=2
      Let enemy(j)\col=8
      Let pilllast=8
      Let kills=0
      EndIf
    Next j
    ;blankout pill, qblit leaves to big a black box
    Boxf pill(i)\oldx,pill(i)\oldy,pill(i)\oldx+8,pill(i)\oldy+4,0
  EndIf

EndIf
Next i
Return

.enemyupdate
If enemy(e)\subpix=0 AND enemy(e)\state<4
  If e=0 Then c1+1
  enemy(e)\xs=0:enemy(e)\ys=0
  If enemy(e)\direction=4 Then enemy(e)\y-1
  If enemy(e)\direction=2 Then enemy(e)\y+1
  If enemy(e)\direction=8 Then enemy(e)\x-1
  If enemy(e)\direction=1 Then enemy(e)\x+1
  enemy(e)\x=QWrap(enemy(e)\x,0,9)
  Let exit=map(enemy(e)\x,enemy(e)\y)
  ;workout where enemy should head depending on its state
  If enemy(e)\state=1 Then enemy(e)\xdest=player\x:enemy(e)\ydest=player\y
  If enemy(e)\state=2 Then enemy(e)\xdest=player\x+4:enemy(e)\ydest=player\y+3:enemy(e)\col-1
  If enemy(e)\state=3 Then enemy(e)\xdest=4:enemy(e)\ydest=4
  enemy(e)\xdest=QWrap(enemy(e)\xdest,0,9)
  enemy(e)\ydest=QWrap(enemy(e)\ydest,0,7)
  ;setup variables for choosing direction (should learn functions?)
  Let olddirection=enemy(e)\direction
  Let ydest=enemy(e)\ydest:Let ypos=enemy(e)\y
  Let xdest=enemy(e)\xdest:Let xpos=enemy(e)\x
  Gosub choosedirection
  Let enemy(e)\direction=direction
EndIf

;move enemy depending on direction
If enemy(e)\direction=4 Then enemy(e)\ys-enemy(e)\spd:enemy(e)\frame=33+(e*2)
If enemy(e)\direction=2 Then enemy(e)\ys+enemy(e)\spd:enemy(e)\frame=41+(e*2)
If enemy(e)\direction=8 Then enemy(e)\xs-enemy(e)\spd:enemy(e)\frame=49+(e*2)
If enemy(e)\direction=1 Then enemy(e)\xs+enemy(e)\spd:enemy(e)\frame=57+(e*2)
enemy(e)\subpix+enemy(e)\spd
;after 32 pixels decide a direction again
If enemy(e)\subpix >=32 Then enemy(e)\subpix=0

If enemy(e)\state=1 ; chasing player
  If e=c2 ; only check enemy collision once every 3 frames
    If RectsHit(enemy(e)\x*32+enemy(e)\xs,enemy(e)\y*32+enemy(e)\ys,10,10,player\x*32+player\xs,player\y*32+player\ys,16,16)
      gamestate=2 ; change gamestate to gameover
      player\col=0 ; reset this to use as a timer
      player\frame=74 ;start frame ready for gameover
      Sound 8,%0101
    EndIf
  EndIf
EndIf

If enemy(e)\state=2 ; being chased
  If enemy(e)\direction=4 Then enemy(e)\frame=39
  If enemy(e)\direction=2 Then enemy(e)\frame=47
  If enemy(e)\direction=8 Then enemy(e)\frame=55
  If enemy(e)\direction=1 Then enemy(e)\frame=63
  If enemy(e)\col=0 Then enemy(e)\state=1
  If e=c2 ; only check enemy collision once every 3 frames
    If RectsHit(enemy(e)\x*32+enemy(e)\xs,enemy(e)\y*32+enemy(e)\ys,10,10,player\x*32+player\xs,player\y*32+player\ys,16,16)
      enemy(e)\state=3 ;change enemy to ghosted
      enemy(e)\col=0
      kills+1
      score+5*kills
      If kills=3 Then score+5
      Gosub hud
      Sound 5,%0101
    EndIf
  EndIf
EndIf

If enemy(e)\state>=3 ; ghosted running to centre or in centre
  enemy(e)\frame=65
  ;all these ifs are naff!
  If enemy(e)\subpix>4 Then enemy(e)\frame+1
  If enemy(e)\subpix>9 Then enemy(e)\frame+1
  If enemy(e)\subpix>14 Then enemy(e)\frame+1
  If enemy(e)\subpix>19 Then enemy(e)\frame+1
  If enemy(e)\subpix>24 Then enemy(e)\frame+1
  If enemy(e)\subpix>29 Then enemy(e)\frame+1
EndIf

If enemy(e)\state=3 ; ghosted running to centre
  If enemy(e)\x=4 AND enemy(e)\y=4 ;are we at centre?
    enemy(e)\state=4 ; change state to centre
    enemy(e)\col=0
    enemy(e)\xs=0
    enemy(e)\ys=0
  EndIf
EndIf

If enemy(e)\state=4 ;ghosted in centre
  enemy(e)\direction=0
  If enemy(e)\subpix=0 Then enemy(e)\col+1
  If enemy(e)\col=15
    If pilllast=0
    enemy(e)\state=1 ;back to normal
    Else
    enemy(e)\state=2 ;running away
    enemy(e)\col=pilllast
    EndIf
  EndIf
EndIf

If enemy(e)\state<3 ; normal animation for states<3
  If enemy(e)\subpix>7 Then enemy(e)\frame+1
  If enemy(e)\subpix>15 Then enemy(e)\frame-1
  If enemy(e)\subpix>23 Then enemy(e)\frame+1
EndIf

DisplaySprite 0,enemy(e)\frame+gfx,enemy(e)\x*32+enemy(e)\xs+xoff,enemy(e)\y*32+enemy(e)\ys+yoff,e*2+2
Return

.pillupdate
If pill(p)\state=0
  p+1:p=QWrap(p,0,12)
  If pillseaten=12 Then Return
  Goto pillupdate
EndIf
pill(p)\spd=pillspeed
If pill(p)\subpix=0
  pill(p)\xs=0:pill(p)\ys=0
  If pill(p)\direction=4 Then pill(p)\y-1
  If pill(p)\direction=2 Then pill(p)\y+1
  If pill(p)\direction=8 Then pill(p)\x-1
  If pill(p)\direction=1 Then pill(p)\x+1
  pill(p)\x=QWrap(pill(p)\x,0,9)
  Let exit=map(pill(p)\x,pill(p)\y)
  pill(p)\xdest=player\x+4:pill(p)\ydest=player\y+3
  pill(p)\xdest=QWrap(pill(p)\xdest,0,9)
  pill(p)\ydest=QWrap(pill(p)\ydest,0,7)
  Let olddirection=pill(p)\direction
  Let ydest=pill(p)\ydest:Let x=pill(p)\x
  Let xdest=pill(p)\xdest:Let y=pill(p)\y
  Gosub choosedirection
  Let pill(p)\direction=direction
EndIf

If pill(p)\direction=4 Then pill(p)\ys-pillspeed
If pill(p)\direction=2 Then pill(p)\ys+pillspeed
If pill(p)\direction=8 Then pill(p)\xs-pillspeed
If pill(p)\direction=1 Then pill(p)\xs+pillspeed
If pill(p)\direction>0 Then pill(p)\subpix+pillspeed
If pill(p)\subpix >=32 Then pill(p)\subpix=0

pill(p)\frame=72

If p>7
  If pill(p)\col>0 Then Let pill(p)\frame=73
  pill(p)\col+1:pill(p)\col=QWrap(pill(p)\col,0,3)
EndIf
  If pill(p)\x*32+pill(p)\xs+xoff > 6 AND pill(p)\x*32+pill(p)\xs+xoff<314
   Boxf pill(p)\oldx,pill(p)\oldy,pill(p)\oldx+8,pill(p)\oldy+4,0
   Blit pill(p)\frame+gfx,pill(p)\x*32+pill(p)\xs+xoff,pill(p)\y*32+pill(p)\ys+yoff
   pill(p)\oldx=pill(p)\x*32+pill(p)\xs+xoff-3:pill(p)\oldy=pill(p)\y*32+pill(p)\ys+yoff-3
  EndIf
Return


.canigo
;player look up map reference and allow u,d,l or r
If exit=0 Then uok=1:dok=1:lok=1:uok=1
If exit=8 Then uok=1:dok=1:lok=0:rok=1
If exit=4 Then uok=0:dok=1:lok=1:rok=1
If exit=2 Then uok=1:dok=0:lok=1:rok=1
If exit=1 Then uok=1:dok=1:lok=1:rok=0
If exit=0 Then uok=1:dok=1:lok=1:rok=1
If exit=9 Then uok=1:dok=1:lok=0:rok=0
If exit=6 Then uok=0:dok=0:lok=1:rok=1
If exit=12 Then uok=0:dok=1:lok=0:rok=1
If exit=5 Then uok=0:dok=1:lok=1:rok=0
If exit=10 Then uok=1:dok=0:lok=0:rok=1
If exit=3 Then uok=1:dok=0:lok=1:rok=0
If exit=14 Then uok=0:dok=0:lok=0:rok=1
If exit=11 Then uok=1:dok=0:lok=0:rok=0
If exit=13 Then uok=0:dok=1:lok=0:rok=0
If exit=7 Then uok=0:dok=0:lok=1:rok=0
If exit=15 Then uok=0:dok=0:lok=0:rok=0
Return

.choosedirection
;AI for ghosts and pills, not very elegant!
;The 15 cases are for 15 different tile types
Select exit
  Case 0
    :c0
    r=Int(Rnd(2))
    If r=0
      If ypos <= ydest Then direction=2
      If ypos > ydest Then direction=4
    Else
      If xpos <= xdest Then direction=1
      If xpos > xdest Then direction=8
    EndIf
    If direction+olddirection=6 Then Goto c0
    If direction+olddirection=9 Then Goto c0
  Case 1
    :c1
    r=Int(Rnd(2))
    If r=0
      If ypos <= ydest Then direction=2
      If ypos > ydest Then direction=4
    Else
      direction=8
    EndIf
    If direction+olddirection=6 Then Goto c1
    If direction+olddirection=9 Then Goto c1
  Case 2
    :c2
    r=Int(Rnd(2))
    If r=0
      If xpos < xdest Then direction=1
      If xpos > xdest Then direction=8
      If xpos = xdest
        r=Int(Rnd(2))
        If r=0 Then direction=1
        If r=1 Then direction=8
      EndIf
    Else
      direction=4
    EndIf
    If direction+olddirection=6 Then Goto c2
    If direction+olddirection=9 Then Goto c2
  Case 3
    direction=8
    If olddirection=1 Then direction=4
    If olddirection=2 Then direction=8
  Case 4
    :c4
    r=Int(Rnd(2))
    If r=0
      If xpos <= xdest Then direction=1
      If xpos > xdest Then direction=8
    Else
      direction=2
    EndIf
    If direction+olddirection=6 Then Goto c4
    If direction+olddirection=9 Then Goto c4
  Case 5
   direction=8
   If olddirection=4 Then direction=8
   If olddirection=1 Then direction=2
  Case 6
    ;If olddirection=0 Then olddirection=8
    ;this is if we are coming out of a spinner from top or bottom
    If olddirection=4 OR olddirection=2 OR olddirection=0
      r=Int(Rnd(2))
      If r=0 Then direction=1
      If r=1 Then direction=8
    Else
      direction=olddirection
    EndIf
  Case 7
    direction=8
  Case 8
    :c8
    r=Int(Rnd(2))
    If r=0
      If ypos <= ydest Then direction=2
      If ypos > ydest Then direction=4
    Else
      direction=1
    EndIf
    If direction+olddirection=6 Then Goto c8
    If direction+olddirection=9 Then Goto c8
  Case 9
    If olddirection=1 OR olddirection=8 OR olddirection=0
      r=Int(Rnd(2))
      If r=0 Then direction=4
      If r=1 Then direction=2
    Else
      direction=olddirection
    EndIf
  Case 10
    direction=1
    If olddirection=2 Then direction=1
    If olddirection=8 Then direction=4
  Case 11
    direction=4
  Case 12
    direction=1
    If olddirection=8 Then direction=2
    If olddirection=4 Then direction=1
  Case 13
    direction=2
  Case 14
    direction=1
End Select
Return

.levelfinish
;display the level end animation and flash the maze
i+1
player\col+1:player\col=QWrap(player\col,0,8)
If player\col=0
    player\frame+1
    DisplayPalette 0,0
EndIf

If player\frame = 74 PalRGB 0,5,15,0,15:PalRGB 0,1,15,0,15
If player\frame = 75 PalRGB 0,5,15,15,0:PalRGB 0,1,15,15,0
player\frame=QWrap(player\frame,74,76)
For e=0 To 2:DisplaySprite 0,16,340,0,e*2+2:Next e
DisplaySprite 0,player\frame+gfx,player\x*32+player\xs+xoff,player\y*32+player\ys+yoff,0
If i=150 Then i=0:level+1:gamestate=0:PalRGB 0,5,15,0,15:DisplayPalette 0,0
Return

.level
;level finish, increment gamespeed
speed=level*0.2
For e=0 To 2
enemy(e)\spd+(speed)
If enemy(e)\spd>3.6 Then enemy(e)\spd=3.6
Next e
player\spd+(speed)
If player\spd>3.6 Then player\spd=3.6
pillspeedinc=player\spd/11
pillspeed=pillspeedinc*3
Return

.spinner
;spin the spinner, the map walls are kinda binary made up
spinner+1:spinner=QWrap(spinner,0,4)
Select spinner
  Case 1
    map(4,4)=13;middle
    map(4,5)-4 ;open bottom
    map(5,4)+8 ;close right
  Case 2
    map(4,4)=7;middle
    map(4,5)+4;close bottom
    map(3,4)-1;open left
  Case 3
    map(4,4)=11
    map(3,4)+1 ;close left
    map(4,3)-2 ;open top
  Case 0
    map(4,4)=14
    map(4,3)+2 ;close top
    map(5,4)-8 ;open right
End Select
Use BitMap 0
;blit the spinner and surrounding tiles
Blit map(4,3),4*32+xoff-16,3*32+yoff-16
Blit map(5,4),5*32+xoff-16,4*32+yoff-16
Blit map(4,5),4*32+xoff-16,5*32+yoff-16
Blit map(3,4),3*32+xoff-16,4*32+yoff-16
Blit map(4,4),4*32+xoff-16,4*32+yoff-16
Return

.counters
c1=QWrap(c1,0,2)
c2+1:c2=QWrap(c2,0,3) ;enemy collision 1 in 3 and pillcollide
c3+1:c3=QWrap(c3,0,10) ;player sound timing

;subtract the timer for enemy being chased
If enemy(0)\subpix=0 AND pilllast > 0 Then pilllast-1

;spin the spinner and allign it to enemy movement
If enemy(0)\subpix=0
  c4+1:c4=QWrap(c4,0,2)
  If c4=1
    Gosub spinner
  EndIf
EndIf

;flash the ghosts when being chased
If pilllast>3 OR pilllast=2
    PalRGB 0,29,15,9,15
    PalRGB 0,20,15,3,15
  DisplayPalette 0,0
EndIf
If pilllast=3 OR pilllast=1
    PalRGB 0,29,0,15,15
    PalRGB 0,20,0,10,10
  DisplayPalette 0,0
EndIf

Return

.hud
;update scores
BitMapOutput 0
If score > highscore(tmap) Then highscore(tmap)=score
Colour 7
Locate 8,1
Print score
Locate 33,1
Print highscore(tmap)
Return

.gfxswitch
;check if space pressed and change graphics set
If butcount=0
  If RawStatus(64)=-1
  butcount=5
    If gfx=0
    gfx=81
    Else
    gfx=0
    EndIf
  EndIf
Else
butcount-1
EndIf
;check if escape is pressed and return to title screen
If RawStatus(69)=-1 Then gamestate=4
Return

.titlescreen
For e=0 To 6:DisplaySprite 0,16,340,0,e:Next e
Use BitMap 1
DisplayBitMap 0,1
DisplayPalette 0,1
Restore messages
BitMapOutput 1
Locate 0,7
Colour 1
Print"Released exclusively for Amigathon 2018"
While Joyb(1)=0
  mss+1
  mss=QWrap(mss,0,100)
  If mss=1
    Read mes$
    If mes$=" " Then Restore messages
  EndIf
  Locate 0,30
  Colour 1
  Print mes$
  Locate 1,10
  Colour 2
  Print"Joystick <> selects map - fire starts"
  Locate 15,11
  Print"Map #"
  Locate 21,11
  Colour 6
  Print tmap+1
  Locate 14,17
  Colour 4
  Print"Highscores"
  For i=0 To 7
    Colour 5
    Locate 18,19+i
    Print i+1
    Locate 14,19+i
    Print "Map"
    Colour 7
    Locate 20,19+i
    Print highscore(i)
  Next i
  ;check for changing the map
  If dbounce>0 Then dbounce-1
  If dbounce=0
    If Joyr(1)=6 Then tmap-1
    If Joyr(1)=2 Then tmap+1
    dbounce=5
  EndIf
  tmap=QWrap(tmap,0,8)
  VWait
Wend
lmap=QWrap(tmap,0,4)
If tmap < 4
  flash=0
  Else
  flash=1
EndIf
gamestate=0
Return

.messages
Data$ "Released exclusively for Amigathon 2018"
Data$ " A port of the  Philips videopac game  "
Data$ "      Written by Gary James 2018       "
Data$ "        Graphics by PacManRed          "
Data$ "  Playtested by Becky, Tara and Alfie  "
Data$ " Don't forget, donate at amigathon.com "
Data$ " "


.mapdata
.map0
Data 12,06,04,05,12,06,04,06,05
Data 08,04,01,08,02,06,00,04,01
Data 09,09,10,00,05,12,03,09,09
Data 10,01,12,00,02,00,05,08,03
Data 06,00,01,09,00,01,08,00,06
Data 12,03,08,00,06,00,03,10,05
Data 10,06,03,10,07,10,06,06,03
.map1
Data 12,07,12,05,12,07,12,06,05
Data 08,06,01,10,01,12,02,04,03
Data 10,05,08,06,00,03,12,02,05
Data 14,00,03,12,02,04,03,12,03
Data 06,00,06,01,00,00,06,00,06
Data 13,08,05,10,06,03,12,02,05
Data 10,03,10,06,06,06,03,14,03
.map2
Data 12,05,14,05,12,06,05,12,05
Data 09,08,04,01,08,05,08,03,09
Data 08,01,09,08,03,09,08,05,09
Data 11,08,01,08,06,00,03,08,03
Data 06,01,08,01,00,00,05,08,06
Data 12,01,09,09,12,03,08,02,05
Data 11,10,03,10,02,07,10,06,03
.map3
Data 13,14,06,06,05,12,06,04,07
Data 08,04,06,04,03,08,05,08,05
Data 09,10,05,08,05,11,08,01,09
Data 10,04,02,03,11,12,03,10,03
Data 06,03,12,05,00,03,12,04,06
Data 12,06,01,09,12,06,01,10,05
Data 10,06,02,03,10,06,02,06,03
