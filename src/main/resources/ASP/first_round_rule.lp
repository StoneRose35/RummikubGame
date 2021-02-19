%%
%%
%% Additional rule for the first round: sum of all number of the figures put on the table must
%% be greater or equal 30
%%
sum_on_table(S) :- S = #sum{N, C, I : ontable(N,C,I), onshelf(N,C,I)}.
sum_jokers(S) :- S= #sum{N,C, I: ontable(N,C,I), onshelf(I-2)}.
:- sum_on_table(S), sum_jokers(S2), (S+S2)<31.
