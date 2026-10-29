#!/usr/bin/env python3
"""
Generate the one-page project proposal PDF for Rainy Day (CPSI 36703, Team 4).

Renders an SVG at US Letter (612x792pt) and converts it to PDF, so the layout
is deterministic and always fits on a single page.

Usage:
    python3 gen_proposal.py proposal.svg
    cairosvg proposal.svg -o proposal.pdf --dpi 72

Requires: Pillow (text measurement for line wrapping), cairosvg, and the
DejaVu Sans font at /usr/share/fonts/truetype/dejavu/.

Note the --dpi 72 flag: cairosvg defaults to 96 DPI, which silently produces a
459x594pt page instead of Letter.
"""
from PIL import ImageFont
import math, sys
R=ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf",100)
B=ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf",100)
def esc(s): return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;")
def w(t,sz,bold=False): return (B if bold else R).getlength(t)*sz/100.0
def wrap(t,sz,width,bold=False):
    out,cur=[],""
    for word in t.split():
        trial=(cur+" "+word).strip()
        if w(trial,sz,bold)<=width: cur=trial
        else: out.append(cur); cur=word
    if cur: out.append(cur)
    return out
INK="#1a1a1a"; MUT="#5b5b5b"; ACC="#1f5c8b"; RULE="#c8c8c8"
SUN="#e8a90c"; BLU="#3d8fc4"; AMB="#c07a0a"; GRY="#9aa3a8"
F="DejaVu Sans"; M=54; CW=612-2*M
o=['<svg xmlns="http://www.w3.org/2000/svg" width="612" height="792" viewBox="0 0 612 792">',
   '<rect width="612" height="792" fill="#ffffff"/>']
def txt(x,y,s,sz,col=INK,bold=False,anchor="start",ls=0):
    a=f' text-anchor="{anchor}"' if anchor!="start" else ""
    l=f' letter-spacing="{ls}"' if ls else ""
    fw=' font-weight="700"' if bold else ""
    return f'<text x="{x}" y="{y:.1f}" font-family="{F}" font-size="{sz}"{fw} fill="{col}"{a}{l}>{esc(s)}</text>'
def para(y,t,sz=9.4,lh=12.6,col=INK,width=CW,x=M):
    for ln in wrap(t,sz,width): o.append(txt(x,y,ln,sz,col)); y+=lh
    return y
def head(y,t): o.append(txt(M,y,t,8.6,ACC,True,ls=1.3)); return y+13
y=64
o.append(txt(M,y,"Rainy Day",27,INK,True))
o.append(txt(612-M,y,"PROJECT PROPOSAL",9,ACC,True,anchor="end",ls=1.2))
o.append(txt(612-M,y+14,"Initial draft · 25 September 2026",8.4,MUT,anchor="end"))
y+=20
o.append(txt(M,y,"Your money, as a weather forecast.",11.5,ACC)); y+=16
o.append(txt(M,y,"CPSI 36703 Mobile Application Development  ·  Team 4  ·  Daniel Cronin & Xander Parker",8.4,MUT)); y+=10
o.append(f'<line x1="{M}" y1="{y}" x2="{612-M}" y2="{y}" stroke="{RULE}" stroke-width="1"/>'); y+=20
y=head(y,"THE PROBLEM")
y=para(y,"Most people know roughly what they earn and what they owe, but not how the two line up in time. Financial trouble is rarely caused by the monthly total — it is caused by a cluster of charges landing in the gap before the next paycheck. Existing budgeting apps answer “how much did I spend on groceries?” They do not answer the question people actually lose sleep over: “am I going to be okay this week?”")
y+=9
y=head(y,"THE SOLUTION")
y=para(y,"Rainy Day reads your upcoming bills and income the way a weather app reads the sky. You enter recurring charges once. The app lays everything ahead on a vertical timeline anchored to today, and derives a daily condition from what is due and when you are next paid — presented in the familiar daily and weekly forecast layout.")
y+=16
for i,(lab,col) in enumerate([("Clear",SUN),("Fair",SUN),("Cloudy",GRY),("Rain",BLU),("Storm",AMB)]):
    sw=CW/5; cx=M+sw*i+sw/2; cy=y+10
    if i==0:
        o.append(f'<circle cx="{cx:.1f}" cy="{cy:.1f}" r="5.5" fill="{col}"/>')
        for k in range(8):
            a=math.radians(k*45)
            o.append(f'<line x1="{cx+math.cos(a)*8:.1f}" y1="{cy+math.sin(a)*8:.1f}" x2="{cx+math.cos(a)*10.5:.1f}" y2="{cy+math.sin(a)*10.5:.1f}" stroke="{col}" stroke-width="1.6" stroke-linecap="round"/>')
    else:
        if i==1: o.append(f'<circle cx="{cx+4:.1f}" cy="{cy-4:.1f}" r="4.5" fill="{SUN}"/>')
        cc = GRY if i in (1,2) else ("#8fb8d0" if i==3 else "#7d8a92")
        o.append(f'<ellipse cx="{cx-3:.1f}" cy="{cy+1:.1f}" rx="5.5" ry="5" fill="{cc}"/>')
        o.append(f'<ellipse cx="{cx+3:.1f}" cy="{cy:.1f}" rx="6.5" ry="6" fill="{cc}"/>')
        o.append(f'<rect x="{cx-8:.1f}" y="{cy+1:.1f}" width="16" height="5.5" rx="2.7" fill="{cc}"/>')
        if i==3:
            for dx in (-4,0,4): o.append(f'<line x1="{cx+dx:.1f}" y1="{cy+9:.1f}" x2="{cx+dx-1:.1f}" y2="{cy+13:.1f}" stroke="{BLU}" stroke-width="1.6" stroke-linecap="round"/>')
        if i==4:
            o.append(f'<path d="M {cx+1:.1f} {cy+8:.1f} L {cx-3:.1f} {cy+14:.1f} L {cx:.1f} {cy+14:.1f} L {cx-2:.1f} {cy+19:.1f} L {cx+4:.1f} {cy+12:.1f} L {cx+1:.1f} {cy+12:.1f} Z" fill="{AMB}"/>')
    o.append(txt(cx,y+41,lab,8.2,MUT,anchor="middle"))
y+=56
y=head(y,"HOW THE FORECAST WORKS")
y=para(y,"For each day: obligations = charges due that day; projected = balance + income received − obligations; buffer = projected − comfort floor. Buffer and the density of nearby charges map onto the five conditions above. This is deliberately transparent arithmetic rather than machine learning: a user can ask why Thursday is stormy and get a straight answer — three bills totalling $340 land two days before payday. In a money app, trust matters more than sophistication.")
y+=9
y=head(y,"WHAT WE WILL BUILD THIS SEMESTER")
for b in ["Timeline — everything ahead, ordered by proximity, with a running balance",
          "Forecast — today’s condition and the week ahead, in the familiar weather layout",
          "Add / Edit — name, amount, date, direction, and recurrence",
          "Calendar — month view showing each day’s condition at a glance"]:
    o.append(f'<circle cx="{M+3}" cy="{y-3:.1f}" r="1.8" fill="{ACC}"/>')
    y=para(y,b,9.4,12.6,INK,CW-16,M+14)
y+=3
y=para(y,"Deliberately out of scope: bank integration, accounts, sync, notifications, and machine learning. Those belong in the roadmap we pitch, not the build we demo — which is what keeps the app finishable.",8.8,12,MUT)
y+=10
y=head(y,"PLATFORM")
y=para(y,"Kotlin with Jetpack Compose and Material 3. Local storage only — no backend, no accounts, and no third-party financial data, which keeps the privacy story simple. Built and demonstrated in Android Studio.")
y+=9
y=head(y,"MILESTONES, AGAINST THE COURSE SCHEDULE")
y=para(y,"Weeks 6–8: data model and the Add / Edit entry screen, ahead of the midterm. Weeks 10–12: timeline and forecast scoring, built alongside the MVVM and data-persistence material. Weeks 13–16: calendar view, polish, and the final presentation.")
y+=9
y=head(y,"TEAM")
y=para(y,"Daniel Cronin — data model, forecast logic, repository and project workflow.   Xander Parker — interface design and Compose screens, forecast presentation.")
y=para(y,"Code and tasks are tracked in a shared GitHub repository; coordination happens in a team chat channel.",8.8,12,MUT)
o.append(f'<line x1="{M}" y1="744" x2="{612-M}" y2="744" stroke="{RULE}" stroke-width="1"/>')
o.append(txt(M,757,"github.com/cpsi36703-team4",8,MUT))
o.append(txt(612-M,757,"Page 1 of 1",8,MUT,anchor="end"))
o.append('</svg>')
open(sys.argv[1],"w",encoding="utf-8").write("\n".join(o))
print("content ends y=%.1f  footer 744  fits one page: %s" % (y, y<736))
