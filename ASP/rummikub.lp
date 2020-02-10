% 1: black, 2: red, 3: yellow, 4: blue

% define figure attributes and figures
color(1).
color(2).
color(3).
color(4).
number(1..13).
idx(1..2).

fig(N,C,I) :- color(C), number(N), idx(I).



%%%
%%%
%%% Player choice: in principle each figure on the shelf can be put on the table
%%%
%%%

0 { ontable(N,C,I) } 1 :- onshelf(N,C,I).
1 { ontable(J) } 1 :- onshelf(J). 


%%
%%
%% Handle Joker: a joker on the table can represent any number and color
%% it has a special index taken from it's own index
1 { ontable(N,C,Ij+2): number(N),color(C) } 1 :- ontable(Ij).


%%%
%%%
%%% Combine figures on table into series (same color) and collections (same number)
%%%
%%%

%%
%% Series of same color
%%

% put each figure which lies in a number range in a series going from L to H or not.
% Predicate: 
%    L: Lowest number in series
%    H: Highest number in series
%    N: Number of element in series (predicate exist for each element in series)
%    C: Color of the Elements in the Serie
%    I3: Instance of Element in series
%    I: instance of series itself, a serie with the same Color and L, H can exists twice
0 { series(L,H,N,C,I3,I) } 1 :- ontable(L,C,I1),ontable(H,C,I2), ontable(N,C,I3), L<H, N>L-1, N<H+1,idx(I). 

% count how many element exist in a series from L to H
sum_series(L,H,C,Q,I):- series(L,H,_,C,_,I), Q = #sum{1, N,I3: series(L,H,N,C,I3,I)}.

% make an answer set invalid if it contains series with less than three elements
:- sum_series(_,_,_,Q,_), Q <3.

% make an answer set invalid if a series contains gaps or twice the same number
:- sum_series(L,H,_,Q,_), Q != H-L+1.

%%
%% collections of same number
%%

% Predicate:
%    N: number of the collection
%    C: color of the element in the collection
%    I1: instance of the element in the collection
%    I: instance of the collection itself, the collection can exist twice
0 { collection(N,C,I1,I) } 1 :- ontable(N,C,I1),idx(I). 

sum_collection(N,Q,I):- collection(N,_,_,I), Q = #sum{1, C2: collection(N,C2,_,I)}.

% no collections with less than 3 elements
:- sum_collection(_,Q,_), Q<3.

% do not use the same number and color in the same collection
:- collection(N,C,I1,I),collection(N,C,I2,I),I1!=I2.


% remove multiple usages of a figure
% in a series and in a collection
:- ontable(N,C,I), collection(N,C,I,_), series(_,_,N,C,I,_).

% in two series
:- series(L1,H1,N,C,I,Is1), series(L2,H2,N,C,I,Is2), L1!=L2.
:- series(L1,H1,N,C,I,Is1), series(L2,H2,N,C,I,Is2), H1!=H2.
:- series(L1,H1,N,C,I,Is1), series(L2,H2,N,C,I,Is2), Is1!=Is2.

% in two collections
:- collection(N,C,I,Ic1), collection(N,C,I,Ic2), Ic1!=Ic2.

% remove answer sets where a figure on table is neither in a collection or a series
:- ontable(N,C,I),not series(_,_,N,C,I,_),not collection(N,C,I,_).


%%
%%
%% Additional rule for the first round: sum of all number of the figures put on the table must
%% be greater or equal 30
%%
sum_on_table(S) :- S = #sum{N, C, I : ontable(N,C,I), onshelf(N,C,I)}.
sum_jokers(S) :- S= #sum{N,C, I: ontable(N,C,I), onshelf(I-2)}.
%:- sum_on_table(S), sum_jokers(S2), (S+S2)<31.

%%%
%%%
%%% Optimize: the solution is best if most of the figures on the shelf could be placed on the table
%%%
%%%
remaining(N,C,I) :- onshelf(N,C,I), not ontable(N,C,I).
remaining(J) :- onshelf(J), not ontable(J).

#minimize{1@10, N,C,I: remaining(N,C,I)}.
#minimize{1@9, J: remaining(J)}.
#maximize{S@8, S: sum_on_table(S)}.
#maximize{S@7, S: sum_jokers(S)}.


%%%
%%%
%%% Output: display solution
%%%
%%%

#show series/6.

%#show sum_series/5.

#show collection/4.

#show ontable/3.
%#show ontable/1.

%#show sum_collection/3.

#show remaining/3.
#show sum_on_table/1.
